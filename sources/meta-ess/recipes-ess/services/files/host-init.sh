#!/bin/sh

BOOTSERVER=$(cat /proc/net/pnp | grep -o '[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}')

tftp -g -r boot/scripts/setup-$HOSTNAME.sh -l /usr/share/host-init/setup-$HOSTNAME.sh $BOOTSERVER

if [ -e "/usr/share/host-init/setup-$HOSTNAME.sh" ]; then
	chmod +x /usr/share/host-init/setup-$HOSTNAME.sh
	/usr/share/host-init/setup-$HOSTNAME.sh
else
	echo "File /usr/share/host-init/setup-$HOSTNAME.sh not found!" > /dev/kmsg
	echo "Hostname: $HOSTNAME, Bootserver: $BOOTSERVER" > /dev/kmsg
fi

