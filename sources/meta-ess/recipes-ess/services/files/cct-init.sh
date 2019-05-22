#!/bin/sh

# Create /lib64 symlink if it doesn't exist
if [ ! -d "/lib64" ]; then
	ln -s /lib /lib64
fi


# Remove all IOxOS cards and rescan the pci bus
# This is a workaround for hot plug
rescan=0

# Find all unique IOxOS cards by [vendorid:deviceid]
pcie_ioxos=$(dmesg | grep "\[7357\:1002\]" | sort -u)

# Remove all IOxOS cards from pci bus
while read -r line
do
	pcie_addr=$(expr substr "$line" 5 12)
	echo 1 > /sys/bus/pci/devices/$pcie_addr/remove
	rescan=1
done <<< "$pcie_ioxos"

# Rescan pci bus to add the IOxOS cards again
if [ "$rescan" = "1" ]; then
	echo 1 > /sys/bus/pci/rescan
fi

