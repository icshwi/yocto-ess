#!/bin/sh

# Add password to iocuser
echo 'iocuser:$6$icshwi$NmdM0vjS1kd82bEW0NUuaxyttGEHvImV09J7EOL5sufjRdanD7NHuABiH1mzdNo7ZKktoX.1w13rKVcOcCOZi0' | chpasswd -e

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
# End csi user


BOOTSERVER=$(cat /proc/net/pnp | grep -o '[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}')
HOST=$(echo $HOSTNAME | grep -o '^[^.]\+')

tftp -g -r boot/scripts/setup-$HOST.sh -l /usr/share/host-init/setup-$HOST.sh $BOOTSERVER

if [ -e "/usr/share/host-init/setup-$HOST.sh" ]; then
	chmod +x /usr/share/host-init/setup-$HOST.sh
	/usr/share/host-init/setup-$HOST.sh
else
	echo "File /usr/share/host-init/setup-$HOST.sh not found!" > /dev/kmsg
	echo "Hostname: $HOST, Bootserver: $BOOTSERVER" > /dev/kmsg
fi

