#!/bin/sh

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

