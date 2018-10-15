#include <common.h>
#include <i2c.h>
#include <hwconfig.h>
#include <command.h>

int
pev_bmr_conv_11bit_u( unsigned short val)
{
  unsigned short l;
  short h;

  l = val & 0x7ff;
  h = val >> 11;
  h |= 0xffe0;
  h =  ~h + 1;
  return((((int)l*1000)/(1 << h)));
}

int
pev_bmr_conv_11bit_s( unsigned short val)
{
  short h,l;

  l = val & 0x7ff;
  if( l & 0x400) l |= 0xf800;
  h = val >> 11;
  h |= 0xffe0;
  h =  ~h + 1;
  return((((int)l*1000)/(1 << h)));
}

int
pev_bmr_conv_16bit_u( unsigned short val)
{
  return((((int)val*1000)/(1 << 13)));
}

unsigned short
bmr_read( int dev,
          unsigned int reg)
{
  uint8_t i2c_data[2];

  i2c_read( (uint8_t)dev, reg, 1, i2c_data, 2);
  return( (unsigned short)((i2c_data[1]<<8) | i2c_data[0]));
}

void
bmr_write( int dev,
	   unsigned int reg,
	   unsigned short data)
{
  uint8_t i2c_data[2];

  i2c_data[0] = (uint8_t)data;
  i2c_data[1] = (uint8_t)(data>>8);
  i2c_write( (uint8_t)dev, reg, 1, i2c_data, 2);
  return;
}


void show_bmr( int dev)
{
  int old_bus;
  unsigned short data;
  int d0;

  old_bus = i2c_get_bus_num();
  i2c_set_bus_num(CONFIG_BMR463_I2C_BUS);
  data = bmr_read( dev, 0x88);
  d0 = pev_bmr_conv_11bit_u( data);
  printf("  VIN  : %2d.%03d [%04x]\n", d0/1000, d0%1000, data);
  data = bmr_read( dev, 0x8b);
  d0 = pev_bmr_conv_16bit_u( data);
  printf("  VOUT : %2d.%03d [%04x]\n", d0/1000, d0%1000, data);
  data = bmr_read( dev, 0x8c);
  d0 = pev_bmr_conv_11bit_u( data);
  printf("  IOUT : %2d.%03d [%04x]\n", d0/1000, d0%1000, data);
  data = bmr_read( dev, 0x8d);
  d0 = pev_bmr_conv_11bit_s( data);
  printf("  TEMP : %2d.%03d [%04x]\n", d0/1000, d0%1000, data);
  i2c_set_bus_num(old_bus);
  return;
}

void show_thermo( void)
{
	int old_bus;
	char data;
	int temp_loc;
	int temp_rem;

#ifdef CONFIG_THERMO_LM95235
	old_bus = i2c_get_bus_num();
	i2c_set_bus_num(CONFIG_THERMO_I2C_BUS);
	data = i2c_reg_read (CONFIG_THERMO_I2C_ADDR, 0);
	temp_loc = (int)data;
	printf("Board local temperature: %2d\n", temp_loc);
	data = i2c_reg_read (CONFIG_THERMO_I2C_ADDR, 1);
	temp_rem = (int)data;
	printf("T2081 die temperature  : %2d\n", temp_rem);
	i2c_set_bus_num(old_bus);
#else
	printf("Thermometers not supported on this board\n");
#endif

}

void show_max( void)
{
  int old_bus;
  uchar data;
  int i;
  uint reg;
  uint min, max, mean, tmp;

  old_bus = i2c_get_bus_num();
  i2c_set_bus_num(CONFIG_MAX5970_I2C_BUS);
  data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, 0);
  reg = 0;
  min = 0x400;
  max = 0x0;
  mean = 0;
  for( i = 0; i < 1000; i++)
  {
    data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg);
    tmp = (uint)data << 2;
    data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg+1);
    tmp |= (uint)data&3;
    mean += tmp;
    if( tmp > max) max = tmp;
    if( tmp < min) min = tmp;
    //usleep(2000);
  }
  mean = (mean*100)/(6*1024);
  min  = (min*100000)/(6*1024);
  max  = (max*100000)/(6*1024);
  printf("   Current 5V0 (A)    : %d.%03d [%d.%03d - %d.%03d]\n", mean/1000, mean%1000, min/1000, min%1000, max/1000, max%1000);

  reg = 2;
  data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg);
  tmp = (uint)data << 2;
  data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg+1);
  tmp |= (uint)data&3;
  tmp = (tmp*16000)/1024;
  printf("   Voltage 5V0 (V)    : %d.%03d \n", tmp/1000, tmp%1000);

  reg = 4;
  min = 0x400;
  max = 0x0;
  mean = 0;
  for( i = 0; i < 1000; i++)
  {
    data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg);
    tmp = (uint)data << 2;
    data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg+1);
    tmp |= (uint)data&3;
    mean += tmp;
    if( tmp > max) max = tmp;
    if( tmp < min) min = tmp;
    //usleep(2000);
  }
  mean = (mean*200)/(9*1024);
  min  = (min*200000)/(9*1024);
  max  = (max*200000)/(9*1024);
  printf("   Current 3V3 (A)    : %d.%03d [%d.%03d - %d.%03d]\n", mean/1000, mean%1000, min/1000, min%1000, max/1000, max%1000);

  reg = 6;
  data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg);
  tmp = (uint)data << 2;
  data = i2c_reg_read (CONFIG_MAX5970_I2C_ADDR, reg+1);
  tmp |= (uint)data&3;
  tmp = (tmp*16000)/1024;
  printf("   Voltage 3V3 (V)    : %d.%03d \n", tmp/1000, tmp%1000);

  i2c_set_bus_num(old_bus);
  return;
}

int do_conf(cmd_tbl_t *cmdtp, int flag, int argc, char * const argv[])
{
  int rc = 0;

  if (argc <= 1)
  {
    return cmd_usage(cmdtp);
  }
  if (strcmp(argv[1], "show") == 0)
  {
	  show_thermo();
	  printf("BMR463_1 [central FPGA]:\n");
	  show_bmr(CONFIG_BMR463_I2C_ADDR_1);
	  printf("BMR463_2 [T2081]:\n");
	  show_bmr(CONFIG_BMR463_I2C_ADDR_2);
	  printf("BMR463_3 [FMC Vadj]:\n");
	  show_bmr(CONFIG_BMR463_I2C_ADDR_3);
          printf("MAX5970 Voltage Monitor\n");
	  show_max();

  }
  else
  {
    rc = cmd_usage(cmdtp);
  }
  return rc;
}

U_BOOT_CMD(
	conf, CONFIG_SYS_MAXARGS, 1, do_conf,
	"Board configuration",
	"show: display current board configuration\n"
);
