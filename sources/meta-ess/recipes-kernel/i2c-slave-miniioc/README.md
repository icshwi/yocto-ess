This README file contains information on **miniIOC i2c slave backend** recipe
and kernel module.

Kernel module description
=========================
This kernel module was designed for ESS miniIOC project. It implements i2c 
slave logic of appropriate responses to SVC commands.

List of SVC commands and the responses of the backend:

| Command name          | Command code  | Action                                  |
| --------------------- | ------------- |---------------------------------------- |
| SCOMMAND_REBOOT       | 0x12          | shutdown -r now, set REBOOT status      |
| SCOMMAND_POWER        | 0x11          | shutdown -h -P now, set SHUTDOWN status |
| SCOMMAND_HALT         | 0x10          | shutdown -h now, set SHUTDOWN status    |
| SCOMMAND_SHUTDOWN_ACK | 0x04          | set SHUTDOWN_ACK status                 |

Additional commands can be added to `i2c_slave_miniioc_slave_comm` function if
necessary.

The feedback with SVC is implemented through statuses:

| Status name         | Status code  | Description                             |
| ------------------- | ------------ |---------------------------------------- |
| STATUS_RUNNING      | 0x01         | Linux is ok and responding              |
| STATUS_REBOOT       | 0x11         | Reboot signal received                  |
| STATUS_SHUTDOWN     | 0x10         | Shut down signal received               |
| STATUS_SHUTDOWN_ACK | 0x14         | Reques acknowledged, ready for shutdown |

The backend sets these statuses as a response to SVC commands, but if necessary
they can be set manually using sysfs device file:
`/sys/bus/i2c/devices/0-001f/slave-minioc` (note that 0-001f is the default i2c
address of the device and can be changed)

Installation
============
In order to install this module, kernel-module-i2c-slave-miniioc package must
be included in the set of packages to install.

As a part of *meta-minioc* layer, it doesn't require any additional actions. By
default, the layer recipes are configured to compile the sources and mark the 
module for auto-loading.

In the default configuration, the device-tree recipe adds the following i2c 
subnode to the device tree:

    miniioc@1f {
    	compatible = "linux,slave-miniioc";
    	reg = <0x1f>;
    	};

It creates a salve device, associates it with this backend and assigns it to 
the default 0x1f address.

If that recipe is disabled, you need to add this node manually or instantiate 
the device using the following command: 
`echo slave-miniioc 0x001f > /sys/bus/i2c/devices/i2c-0/new-device` 
(note that i2c-0 is the default bus and can be changed)
