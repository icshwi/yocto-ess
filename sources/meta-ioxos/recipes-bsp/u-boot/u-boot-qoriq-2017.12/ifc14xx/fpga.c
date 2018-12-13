/*
* File fpga.c
*
* Purpose:
* Makes command "fpga" available on UBOOT level which is used
* to access PON FPGA registers over P2020 ELB bus.
* There are two main functions supported:
*  1. configure the CENTRAL and IO FPGAs over register
*     ELB_FPGA_CFGCTL and ELB_FPGA_CFGDAT
*  2. initiate reset of the board with correct settings for
*     IDT PCIe switch init over registers ELB_FPGA_CFGCTL and
*     ELB_PCIESW
*
* Both commands, "fpga load" and "fpga reset" will skip operation
* when called without the "force" argument" when
* 1. the FPGA was already configures, so the CONF_DONE bit is set
* 2. the board reset command to PON FPGA was already once given
*    by "fpga reset" command
*
*
* IMPORTANT WARNING:
* At the moment fpga reset mis-use bit 28 of register ELB_FPGA_CFGCTL.
* This shall be changed with new PON FPGA version to another free flag-bit.
* Then this code has to be changed (lines 90, 101) and comment can deleted.
*/

/* - IDENTIFICATION ------------------------------------------------------------
 *
 *   PROJECT	:	IFC1210
 *
 *   DOMAIN	:	u-boot
 *
 *   MODULE	:	board
 *
 *   DESCRIPTION:	see above comment
 *
 *
 *
 *   AUTHOR	:	Mario Jurcevic
 *
 *   HISTORY	:
 *
 *   Revision	Date		Author	Reference	Description
 *
 *   695	31032012	JA84	IFC1210		Use fpga_INIT_MEM[5:0] bit 0
 *							of register ELB_FPGA_CFGCTL for reset
 *
 * -----------------------------------------------------------------------------
 */

#include <common.h>
#include <command.h>
#include <asm/processor.h>
#include <asm/io.h>

#define PCISW_OFFSET    0x14
#define FPGA_OFFSET     0x20
#define PGM_RST         0x2C
#define FPGA_INIT       0x01
#define FPGA_DONE       0x02
#define FPGA_PROG       0x04
#define FPGA_CSI        0x08
#define FPGA_RW         0x10
#define FPGA_ENABLE     0x80

static int fpga_dev = 0;

void
fpga_set_dev( uint dev)
{
  fpga_dev = (dev & 1);
}

static void
fpga_write_io( uint data,
               volatile unsigned long reg_p)
{
  *(uint *)reg_p = data;
  data = *(uint *)reg_p; /* force write cycle to be executed */

  return;
}


static uint
fpga_read_io( volatile unsigned long reg_p)
{
  volatile uint data;

  data = *(uint *)reg_p;

  return( data);
}

static int fpga_enable = 0;
static int fpga_prog = 0;
static int fpga_init = 0;
static int fpga_done = 0;
static int fpga_csi = 0;
static int fpga_rdwr = 0;
static int fpga_count = 0;
static int fpga_mask = 0xffffffff;

void
fpga_reset( int pcisw, int force)
{
  unsigned long io_reg;
  volatile int data;

  /* check if this is first time we issue a fpga reset command form u-boot, if no: skip the reset */
  io_reg = CONFIG_SYS_PON_BASE + FPGA_OFFSET;
  data = fpga_read_io( io_reg);
  if( (force == 0) && (data & 0x00010000) )     /* use bit 16 of ELB_FPGA_CFGCTL reg, fpga_INIT_MEM[5:0] */
  {
    printf("FPGA already resetted once: Therefore skipping FPGA reset request.\n");
  }
  else
  {
    io_reg = CONFIG_SYS_PON_BASE + PCISW_OFFSET;
    fpga_write_io( pcisw, io_reg);

    io_reg = CONFIG_SYS_PON_BASE + FPGA_OFFSET;
    data =  fpga_read_io( io_reg) & 0x0200ffff;
    data |= 0x80010000;     /* set also bit 16 to remember if reset was sent already once*/
    fpga_write_io( data, io_reg);
  }
  return;
}

int
fpga_load( char *buf,
	   uint len,
	   int first,
           int force)
{
  unsigned long io_reg;
  int i, j;
  char *s;
  volatile int data, mask, tmo;

  io_reg = CONFIG_SYS_PON_BASE + FPGA_OFFSET;
  if( first)
  {
    fpga_count   = 0;
    if( fpga_dev)
    {
      fpga_enable = 0x8000;
      fpga_init   = 0x0100;
      fpga_done   = 0x0200;
      fpga_prog   = 0x0400;
      fpga_csi    = 0x4000;
      fpga_rdwr   = 0x2000;
      fpga_mask   = ~0x800ff80;
    }
    else
    {
      fpga_enable = 0x80;
      fpga_init   = 0x01;
      fpga_done   = 0x02;
      fpga_prog   = 0x04;
      fpga_csi    = 0x40;
      fpga_rdwr   = 0x20;
      fpga_mask   = ~0x80080ff;
    }


    /* check if fpga was already configured before, if yes: skip */
    if(force == 0)
    {
        data = fpga_read_io( io_reg);
        if(data & fpga_done)
        {
            printf("FPGA already configured (CONF_DONE), skipping configuration [%d] !!\n", fpga_dev);
            return(0);
        }
    }

    /* initialize load sequence */
    mask = fpga_read_io( io_reg) & fpga_mask;
    //fpga_write_io( mask | fpga_enable | fpga_prog, io_reg);
    fpga_write_io( mask | fpga_enable, io_reg);
    tmo = 100000;
    while( tmo--);
    fpga_read_io( io_reg);

    /* clear prog_bit */
    //fpga_write_io( mask |  fpga_enable, io_reg);
    fpga_write_io( mask |  fpga_enable | fpga_prog, io_reg);
    tmo = 100000;
    while( tmo--);
    fpga_read_io( io_reg);

    /* wait for INIT to go low */
    tmo = 500000;
    while( --tmo)
    {
      data =  fpga_read_io( io_reg);
      //if( !( data & fpga_init))
      if( data & fpga_init)
      {
        break;
      }
    }
    if( !tmo)
    {
      printf("INIT bit still high [%08x]!!\n", data);
      fpga_write_io( fpga_enable | fpga_prog, io_reg); /* clear bit_prog */
      fpga_write_io( fpga_prog, io_reg); /*  disable acces to FPGA */
      return( -1);
    }

    /* set prog bit */
    //fpga_write_io( mask | fpga_enable | fpga_prog, io_reg);
    fpga_write_io( mask | fpga_enable, io_reg);

    /* wait for bit_init to go high */
    tmo = 50000;
    while( --tmo)
    {
      data = fpga_read_io( io_reg);
      //if( data & fpga_init)
      if( !(data & fpga_init))
      {
        break;
      }
      data = 0;
    }
    if( !tmo)
    {
      printf("INIT not high [%08x]!!\n", data);
      /*  disable acces to UFPGA */
      //fpga_write_io( mask | fpga_prog, io_reg)
      fpga_write_io( mask, io_reg);
      return( -1);
    }
  }

  /* set csi and rdwr bit */
  //fpga_write_io( mask | fpga_enable | fpga_prog, io_reg);
  fpga_write_io( mask | fpga_enable | fpga_csi , io_reg);
  data = fpga_read_io( io_reg);
  fpga_write_io( mask | fpga_enable | fpga_csi | fpga_rdwr, io_reg);
  data = fpga_read_io( io_reg);
  printf("Begin to load bitstream [%08x]\n", data);

  s = buf;
  for( j = 0; j < len;)
  {
    //data =  *(long *)s;
    data = (int)(*s++);
    data |= (int)(*s++) << 8;
    data |= (int)(*s++) << 16;
    data |= (int)(*s++) << 24;
    //s += 4; j += 4;
    j += 4;
    if( j == len) s = buf;

    fpga_write_io( data, io_reg + 4); /* program dword */

    /* check end of programming sequence */
    data =  fpga_read_io( io_reg);
    if( data & fpga_done)
    {
      fpga_count += j;
      for( i = 0; i < 100; i++)
      {
	fpga_write_io( 0, io_reg + 4); /* program dword */
      }
      /*  disable acces to FPGA keeping bit_prog high */
      //fpga_write_io( mask | fpga_prog, io_reg);
      fpga_write_io( mask | fpga_enable | fpga_csi, io_reg);
      data = fpga_read_io( io_reg);
      fpga_write_io( mask | fpga_enable, io_reg);
      data = fpga_read_io( io_reg);
      fpga_write_io( mask, io_reg);
      data = fpga_read_io( io_reg);
      return(0);
    }

    /* verify that bit_init is still high */
    data =  fpga_read_io( io_reg);
    //if( !( data & fpga_init))
    if( data & fpga_init)
    {
      /*  disable acces to UFPGA */
      //fpga_write_io( mask | fpga_prog, io_reg);
      fpga_write_io( mask | fpga_enable | fpga_csi, io_reg);
      data = fpga_read_io( io_reg);
      fpga_write_io( mask | fpga_enable, io_reg);
      data = fpga_read_io( io_reg);
      fpga_write_io( mask, io_reg);
      data = fpga_read_io( io_reg);
      printf("INIT bit is low !! [count=0x%x]", fpga_count);
      return( -1);
    }
  }
  fpga_count += j;
  fpga_write_io( mask | fpga_enable | fpga_csi, io_reg);
  data = fpga_read_io( io_reg);
  fpga_write_io( mask | fpga_enable, io_reg);
  data = fpga_read_io( io_reg);
  fpga_write_io( mask, io_reg);
  data = fpga_read_io( io_reg);
  return( 1);

}

int do_fpga (cmd_tbl_t *cmdtp, int flag, int argc, char * const argv[])
{
  ulong addr = 0;
  int fpga = -1;
  int force = 0;

  if (argc < 2)
  {
    goto failure;
  }

  if (strcmp(argv[1],"load") == 0)		/* load */
  {
    if (argc < 4)
    {
      goto failure;
    }
    if (strncmp(argv[2],"io",2) == 0)		/* load */
    {
      fpga = 1;
    }
    else if (strncmp(argv[2],"central",2) == 0)
    {
      fpga = 0;
    }
    if (fpga < 0)
    {
      goto failure;
    }
    addr = simple_strtoul(argv[3], NULL, 16);
    fpga_set_dev( fpga);
    if (strcmp(argv[4],"force") == 0)		/* check if force reload asked */
    {
      force = 1;
    }
    printf ("FPGA load %s [%d] from addr %08lx: ", argv[2], fpga, addr);
    if( fpga_load( (char *)addr, 0x1000000, 1, force))
    {
      printf("Error while loading FPGA\n");
    }
    else
    {
      printf("FPGA load OK\n");
    }

  }
  else if (strncmp(argv[1], "reset", 4) == 0)	/* reset */
  {
    int pcisw;

    pcisw = simple_strtoul(argv[2], NULL, 16);
    if (strcmp(argv[3],"force") == 0)		/* check if force reset asked */
    {
      force = 1;
    }
    printf ("FPGA reset:\n");
    fpga_reset( pcisw, force);
  }
  else
  {
    goto failure;
  }

  return 0;

 failure:
    return cmd_usage(cmdtp);
}

U_BOOT_CMD(
	fpga,	6,	1,	do_fpga,
	"access FPGA(s)",
	"reset <ELB_PCIESWCTL> <force>    - reset FPGA\n"
	"fpga load io <addr> <force>      - load FPGA configuration data\n"
	"fpga load central <addr> <force> - load FPGA configuration data"
);
