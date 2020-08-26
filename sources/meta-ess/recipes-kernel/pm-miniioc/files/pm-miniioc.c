/*
 * Power off module for FPGA MINIIOC
 *
 */

#include <linux/module.h>
#include <linux/platform_device.h>
#include <linux/init.h>
#include <linux/of.h>
#include <linux/gpio.h>
#include <linux/of_gpio.h>
#include <linux/delay.h>
#include <linux/reboot.h>

#define DRIVER_NAME "pm-miniioc"
#define DEVICE_NAME "gpio-poweroff"

int pin;

static void miniioc_power_off(void) {

    if (gpio_request(pin, "Some name")) { //Name it whatever you want
        printk("%s: FAILED gpio_request for %d \r\n", DEVICE_NAME, pin);
    }
    if (gpio_direction_output(pin, 0)) { //The second parameter is the initial value. 0 is low, 1 is high.
        printk("%s: FAILED gpio_direction_output for %d \r\n", DEVICE_NAME, pin);
    }

    //infinite loop sending pulses to SVC to power off the picozed board
    while (1) {
        printk(".");
        gpio_set_value(pin, 1);
        mdelay(100);
        gpio_set_value(pin, 0);
        mdelay(100);
    }
}

static int pmu_restart_notify(struct notifier_block *this,
        unsigned long code, void *unused)
{

    printk("%s: powering off \r\n", DEVICE_NAME);
    miniioc_power_off();

    return NOTIFY_DONE;
}

static struct notifier_block pmu_restart_handler = {
    .notifier_call = pmu_restart_notify,
    .priority = 254,
};


static int pm_miniioc_probe(struct platform_device *dev)
{
    int ret;
    struct device_node *np;
    printk("%s: Installed\r\n", DRIVER_NAME);

    ret = register_restart_handler(&pmu_restart_handler);
    if (ret)
        printk("%s: can't register restart handler err=%d!\r\n", DRIVER_NAME, ret);
    else
        printk("%s: registered restart handler!\r\n", DRIVER_NAME);


    np = dev->dev.of_node;

    // reading int property
    if (of_property_read_u32(np, "gpiopin", &pin) != 0) {
        pr_err("failed to get GPIO from device tree\n");
        return -1;
    }

    // for gpio property <&gpio0 917 0>:
    /*
    pin = of_get_gpio(np, 0);
    if (pin < 0) {
        pr_err("failed to get GPIO from device tree\n");
        return -1;
    }
    */

    printk("%s: using GPIO %d!\r\n", DEVICE_NAME, pin);

    return 0;
}

static int pm_miniioc_remove(struct platform_device *dev)
{
    printk("%s: removed\r\n", DRIVER_NAME);
    return 0;
}

static struct of_device_id pm_miniioc_table[] = {
    {
       .compatible = DEVICE_NAME,
    },
    {0}
};

static struct platform_driver pm_miniioc_driver = {
    .probe = pm_miniioc_probe,
    .remove = pm_miniioc_remove,
    .driver = {
        .name = DRIVER_NAME,
        .owner = THIS_MODULE,
        .of_match_table = of_match_ptr(pm_miniioc_table),
    },
};


module_platform_driver(pm_miniioc_driver);

MODULE_DEVICE_TABLE(of, pm_miniioc_table);

MODULE_DESCRIPTION("Minioc Power off module");
MODULE_LICENSE("GPL v2");
