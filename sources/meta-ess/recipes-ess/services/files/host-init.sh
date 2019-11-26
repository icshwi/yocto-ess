#!/bin/sh

# Check for existence of csi user ...
# Run "id -u" command for csi user, and redirect output + errors to stdout
id -u csi >/dev/null 2>&1

# Check exit code for command { 0 == User exists, 1 == User doesn't exist }
if ! [ $? -eq 0 ]; then

# User not found, now create user ...

# Create csi user
adduser csi
echo 'csi:$6$rounds=656000$dU2LteK8bw34CcUd$UrTE09vKKtQTF6sj8OSrRVzs5uzLlPAgpmsAK3kzSUztiSesIauzB5nHgiG93EdwGNsa4h/jQ6Yn21V28Bs7s0' | chpasswd -e
echo "csi ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/csi
mkdir /home/csi/.ssh

cat << EOF > /home/csi/.ssh/authorized_keys
ecdsa-sha2-nistp521 AAAAE2VjZHNhLXNoYTItbmlzdHA1MjEAAAAIbmlzdHA1MjEAAACFBAF/XCunlPgvj8LdgcFP2iJ8pe/oPfs68ANxYLoHfgNh8xzBkeqaIDfQIhzT1nlZDeaMwqEXI5Q85w2HtOKZSnaDGAHs4SCV1iFtvZyUnOoevAGHF5uyOmjyxveHAR/psvMxVBE/O403PsVdcfpQfVGEwV7se8bCa1S7YiQtN/bLigpgwg== csi-deploy-key
EOF
chown -R csi:csi /home/csi/.ssh
chmod 600 /home/csi/.ssh/authorized_keys

# Disable password auth. SSH logon is via ssh key only.
echo "Disabling password auth in sshd_config"
#sed s/PasswordAuthentication\ yes/PasswordAuthentication\ no/ -i /etc/ssh/sshd_config

echo -e "\nMatch User csi\n    PasswordAuthentication no" >> /etc/ssh/sshd_config

fi
# End csi user


BOOTSERVER=$(cat /proc/net/pnp | grep -o '[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}')
HOST=$(echo $HOSTNAME | grep -o '^[^.]\+')
NETDEV=$(ip addr | awk '/MULTICAST,UP,LOWER_UP/ {print $2}' | sed 's/.$//')
MAC=$(cat /sys/class/net/$NETDEV/address | awk '{ gsub(":", "-") ;  print $0 }')

tftp -g -r boot/scripts/setup-$HOST.sh -l /usr/share/host-init/setup-$HOST.sh $BOOTSERVER
if [ ! -e "/usr/share/host-init/setup-$HOST.sh" ]; then
    tftp -g -r boot/uboot/$MAC/host-init.sh -l /usr/share/host-init/setup-$HOST.sh $BOOTSERVER
fi

if [ -e "/usr/share/host-init/setup-$HOST.sh" ]; then
	chmod +x /usr/share/host-init/setup-$HOST.sh
	/usr/share/host-init/setup-$HOST.sh
else
	echo "File /usr/share/host-init/setup-$HOST.sh not found!" > /dev/kmsg
	echo "Hostname: $HOST, Bootserver: $BOOTSERVER" > /dev/kmsg
fi

