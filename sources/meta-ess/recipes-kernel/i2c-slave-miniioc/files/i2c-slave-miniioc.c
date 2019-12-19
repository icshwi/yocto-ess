/*
 * I2C slave interface for miniIOC
 *
 */

#include <linux/spinlock.h>
#include <linux/module.h>
#include <linux/sysfs.h>
#include <linux/slab.h>
#include <linux/init.h>
#include <linux/i2c.h>
#include <linux/of.h>

#define DRIVER_NAME "i2c-slave-miniioc"
#define DEVICE_NAME "slave-miniioc"

#define SCOMMAND_HALT 0x10
#define SCOMMAND_POWER 0x11
#define SCOMMAND_REBOOT 0x12
#define SCOMMAND_SHUTDOWN_ACK 0x14

#define STATUS_RUNNING 0x01
#define STATUS_REBOOT 0x02
#define STATUS_SHUTDOWN 0x03
#define STATUS_SHUTDOWN_ACK 0x04

char * down_argv[]= { "/sbin/shutdown", "-h", "-P", "now", NULL};
char * reboot_argv[]= { "/sbin/shutdown", "-r", "now", NULL};
char * halt_argv[]= { "/sbin/shutdown", "-h", "now", NULL};

struct mi_slave_interface {
	struct bin_attribute bin;
	spinlock_t status_mutex;
	u8 status;
};

static int i2c_slave_miniioc_slave_comm(struct i2c_client *client,
				     enum i2c_slave_event event, u8 *val)
{
	
	struct mi_slave_interface *miniioc = i2c_get_clientdata(client);

	switch (event) {
	case I2C_SLAVE_WRITE_RECEIVED:
		switch(*val){
		case SCOMMAND_REBOOT:
			miniioc->status = STATUS_REBOOT;
			printk("%s: SVC requested reboot\r\n", DRIVER_NAME);
			call_usermodehelper(reboot_argv[0], reboot_argv, NULL, UMH_NO_WAIT);
			break;
		case SCOMMAND_POWER:
			miniioc->status = STATUS_SHUTDOWN;
			printk("%s: SVC requested shutdown\r\n", DRIVER_NAME);
			call_usermodehelper(down_argv[0], down_argv, NULL, UMH_NO_WAIT);
			break;
		case SCOMMAND_HALT:
			miniioc->status = STATUS_SHUTDOWN;
			printk("%s: SVC requested halt\r\n", DRIVER_NAME);
			call_usermodehelper(halt_argv[0], halt_argv, NULL, UMH_NO_WAIT);
			break;
		case SCOMMAND_SHUTDOWN_ACK:
			miniioc->status = STATUS_SHUTDOWN_ACK;
			printk("%s: SVC requested shutdown acknowledge\r\n", DRIVER_NAME);
			break;
		default:	
			printk("%s: SVC requested unknown action %d\r\n", DRIVER_NAME, *val);
			break;
		}
		break;
	case I2C_SLAVE_READ_PROCESSED:
		/* This event is not supported by the backend */
	case I2C_SLAVE_READ_REQUESTED:
		spin_lock(&miniioc->status_mutex);
		*val = miniioc->status;
		spin_unlock(&miniioc->status_mutex);
		break;
	case I2C_SLAVE_STOP:
		/* No action required */
		break;
	case I2C_SLAVE_WRITE_REQUESTED:
		/* No action required */
		break;
	default:
		break;
	}
	return 0;
}

static ssize_t i2c_slave_miniioc_bin_read(struct file *filp, struct kobject *kobj,
		struct bin_attribute *attr, char *buf, loff_t off, size_t count)
{
	/* Exporting data to the user space is not supported, but this call can be 
	 * used for printing current status of the device */
	struct mi_slave_interface *miniioc;

	miniioc = dev_get_drvdata(container_of(kobj, struct device, kobj));
	printk("%s: Status of the device is %d\r\n", DRIVER_NAME, miniioc->status);
	return 0;
}

static ssize_t i2c_slave_miniioc_bin_write(struct file *filp, struct kobject *kobj,
		struct bin_attribute *attr, char *buf, loff_t off, size_t count)
{
	struct mi_slave_interface *miniioc;

	miniioc = dev_get_drvdata(container_of(kobj, struct device, kobj));

	if (count > 2)
	{
		printk("%s: Can't transmit more than 1 byte\r\n", DRIVER_NAME);
	}else
	{
		spin_lock(&miniioc->status_mutex);
		memcpy(&miniioc->status, buf, 1);
		spin_unlock(&miniioc->status_mutex);
		printk("%s: status chenged to %c \r\n", DRIVER_NAME, buf[0]);
	}

	return count;
}

static int i2c_slave_miniioc_probe(struct i2c_client *client, const struct i2c_device_id *id)
{
	struct mi_slave_interface *miniioc;
	int ret;
	
	printk("%s: Initialization\r\n", DRIVER_NAME);

	miniioc = devm_kzalloc(&client->dev, sizeof(struct mi_slave_interface), GFP_KERNEL);
	if (!miniioc)
		return -ENOMEM;

	//Here we set the initial status of the device. It can be changed by i2c master or from the user space
	miniioc->status = STATUS_RUNNING;

	//Set mutex
	spin_lock_init(&miniioc->status_mutex);

	client->flags |= I2C_CLIENT_SLAVE;
	
	i2c_set_clientdata(client, miniioc);

	//Sysfs api for accesing the device from the user space
	sysfs_bin_attr_init(&miniioc->bin);
	miniioc->bin.attr.name = DEVICE_NAME;
	miniioc->bin.attr.mode = S_IRUSR | S_IWUSR;
	miniioc->bin.read = i2c_slave_miniioc_bin_read;
	miniioc->bin.write = i2c_slave_miniioc_bin_write;
	miniioc->bin.size = 2;

	ret = sysfs_create_bin_file(&client->dev.kobj, &miniioc->bin);
	if (ret)
		return ret;

	ret = i2c_slave_register(client, i2c_slave_miniioc_slave_comm);
	if (ret) {
		sysfs_remove_bin_file(&client->dev.kobj, &miniioc->bin);
		return ret;
	}

	return 0;
};

static int i2c_slave_miniioc_remove(struct i2c_client *client)
{
	struct mi_slave_interface *miniioc = i2c_get_clientdata(client);

	i2c_slave_unregister(client);
	sysfs_remove_bin_file(&client->dev.kobj, &miniioc->bin);
	printk("%s: removed\r\n", DEVICE_NAME);
	return 0;
}

static const struct i2c_device_id i2c_slave_miniioc_id[] = {
	{ DEVICE_NAME, 0 },
	{ }
};
MODULE_DEVICE_TABLE(i2c, i2c_slave_miniioc_id);

static struct i2c_driver i2c_slave_miniioc_driver = {
	.driver = {
		.name = DRIVER_NAME, 
	},
	.probe = i2c_slave_miniioc_probe, 
	.remove = i2c_slave_miniioc_remove, 
	.id_table = i2c_slave_miniioc_id, 
};
module_i2c_driver(i2c_slave_miniioc_driver);

MODULE_DESCRIPTION("Minioc I2C slave communication client");
MODULE_LICENSE("GPL v2");
