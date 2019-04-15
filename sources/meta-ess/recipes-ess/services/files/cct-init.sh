#!/bin/sh

# Create /lib64 symlink if it doesn't exist
if [ ! -d "/lib64" ]; then
	ln -s /lib /lib64
fi

