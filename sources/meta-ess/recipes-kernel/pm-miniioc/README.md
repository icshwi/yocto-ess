This README file contains information on **miniIOC power management driver** recipe
and kernel module.

Kernel module description
=========================
This kernel module was designed for ESS miniIOC project. It allows to request miniIOC fpga
power cycle by running Linux command e.g.`shutdown -P -h now`.


Installation
============
In order to install this module, kernel-module-pm-miniioc package must
be included in the set of packages to install.

As a part of *meta-minioc* layer, it doesn't require any additional actions. By
default, the layer recipes are configured to compile the sources and mark the 
module for auto-loading.

In the default configuration, the device-tree recipe adds the following 
node to the device tree:

    power_ctrl: power_ctrl {
	    status = "okay";
	    compatible = "gpio-poweroff";
        gpiopin = <&gpio0 917 0>;
    };

It creates a power_ctrl device, associates it with this driver.
