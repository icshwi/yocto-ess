/*
 * Copyright 2014 Freescale Semiconductor, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * Version 2 or later as published by the Free Software Foundation.
 */

#include <common.h>
#include <i2c.h>
#include <hwconfig.h>
#include <asm/mmu.h>
#include <fsl_ddr_sdram.h>
#include <fsl_ddr_dimm_params.h>
#include <asm/fsl_law.h>
#include "ddr.h"

DECLARE_GLOBAL_DATA_PTR;

#define DEBUG
#if defined(DEBUG)
void testdram( void);
#endif

void fsl_ddr_board_options(memctl_options_t *popts,
				dimm_params_t *pdimm,
				unsigned int ctrl_num)
{
	const struct board_specific_parameters *pbsp, *pbsp_highest = NULL;
	ulong ddr_freq;

	if (ctrl_num > 1) {
		printf("Not supported controller number %d\n", ctrl_num);
		return;
	}
	if (!pdimm->n_ranks)
		return;

	pbsp = udimms[0];

	/* Get clk_adjust, wrlvl_start, wrlvl_ctl, according to the board ddr
	 * freqency and n_banks specified in board_specific_parameters table.
	 */
	ddr_freq = get_ddr_freq(0) / 1000000;
	while (pbsp->datarate_mhz_high) {
		if (pbsp->n_ranks == pdimm->n_ranks &&
		    (pdimm->rank_density >> 30) >= pbsp->rank_gb) {
			if (ddr_freq <= pbsp->datarate_mhz_high) {
				popts->clk_adjust = pbsp->clk_adjust;
				popts->wrlvl_start = pbsp->wrlvl_start;
				popts->wrlvl_ctl_2 = pbsp->wrlvl_ctl_2;
				popts->wrlvl_ctl_3 = pbsp->wrlvl_ctl_3;
				goto found;
			}
			pbsp_highest = pbsp;
		}
		pbsp++;
	}

	if (pbsp_highest) {
		printf("Error: board specific timing not found");
		printf("for data rate %lu MT/s\n", ddr_freq);
		printf("Trying to use the highest speed (%u) parameters\n",
		       pbsp_highest->datarate_mhz_high);
		popts->clk_adjust = pbsp_highest->clk_adjust;
		popts->wrlvl_start = pbsp_highest->wrlvl_start;
		popts->wrlvl_ctl_2 = pbsp->wrlvl_ctl_2;
		popts->wrlvl_ctl_3 = pbsp->wrlvl_ctl_3;
	} else {
		panic("DIMM is not supported by this board");
	}
found:
	debug("Found timing match: n_ranks %d, data rate %d, rank_gb %d\n"
		"\tclk_adjust %d, wrlvl_start %d, wrlvl_ctrl_2 0x%x, "
		"wrlvl_ctrl_3 0x%x\n",
		pbsp->n_ranks, pbsp->datarate_mhz_high, pbsp->rank_gb,
		pbsp->clk_adjust, pbsp->wrlvl_start, pbsp->wrlvl_ctl_2,
		pbsp->wrlvl_ctl_3);

	/*
	 * Factors to consider for half-strength driver enable:
	 *	- number of DIMMs installed
	 */
	popts->half_strength_driver_enable = 0;
	/*
	 * Write leveling override
	 */
	popts->wrlvl_override = 1;
	popts->wrlvl_sample = 0xf;

	/*
	 * Rtt and Rtt_WR override
	 */
	popts->rtt_override = 0;

	/* Enable ZQ calibration */
	popts->zq_en = 1;

	/* DHC_EN =1, ODT = 75 Ohm */
	popts->ddr_cdr1 = DDR_CDR1_DHC_EN | DDR_CDR1_ODT(DDR_CDR_ODT_75ohm);
	popts->ddr_cdr2 = DDR_CDR2_ODT(DDR_CDR_ODT_75ohm);

	/* optimize cpo for erratum A-009942 */
	popts->cpo_sample = 0x3c;
}

int dram_init(void)
{
	phys_size_t dram_size;

#if defined(CONFIG_SPL_BUILD) || !defined(CONFIG_RAMBOOT_PBL)
	puts("Initializing....using SPD\n");
	dram_size = fsl_ddr_sdram();
#else
	/* DDR has been initialised by first stage boot loader */
	dram_size = fsl_ddr_sdram_size();
#endif

	dram_size = setup_ddr_tlbs(dram_size / 0x100000);
	dram_size *= 0x100000;

	gd->ram_size = dram_size;

#if defined(DEBUG)
	testdram();
#endif

	return 0;
}

#if defined(DEBUG)
void testdram( void)
{
  int i, tmo;
  int ctl, nloop;

  ctl = 0;

	printf("       Performing Hardware DDR test\n");
	if( in_be32(  (volatile unsigned int *)0xfe008d20) == 0x11111111)
	{
	  printf("DDR already initialized...\n");
	  return;
	}
	//printf("work around A-008109\n");
	/* Freescale work around A-008109 [CG] */
	//ctl = in_be32(  (volatile unsigned int *)0xfe008f48); /* read DEBUG_19       */
	//ctl |= 2;                                             /* set bit 30          */
	//out_be32(  (volatile unsigned int *)0xfe008f48, ctl); /* overwrite  DEBUG_19 */
	//ctl = 0x30000000;
	//out_be32(  (volatile unsigned int *)0xfe008f70, ctl); /* write  DEBUG_29     */

	/* prepare data for memory test */
        out_be32(  (volatile unsigned int *)0xfe008d20, 0x11111111); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d24, 0x22222222); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d28, 0x33333333); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d2c, 0x44444444); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d30, 0x55555555); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d34, 0x66666666); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d38, 0x77777777); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d3c, 0x88888888); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d40, 0x99999999); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d44, 0xaaaaaaaa); /* test pattern */
        out_be32(  (volatile unsigned int *)0xfe008d60, 0x00000000); /* start address high */
        out_be32(  (volatile unsigned int *)0xfe008d64, 0x7f000000); /* start address low  */
        out_be32(  (volatile unsigned int *)0xfe008d68, 0x00000000); /* end address high */
        out_be32(  (volatile unsigned int *)0xfe008d6c, 0x7f100000); /* end address low  */
        //out_be32(  (volatile unsigned int *)0xfe008d00, 0x80000200); /* start memory test */
#ifdef DEBUG
	nloop = 4;
#else
	nloop = 1;
#endif
	for( i = 0; i < nloop; i++)
	{
	  printf("       Test mode %d...", i);
	  out_be32(  (volatile unsigned int *)0xfe008d00, 0x80000000 |  (i<<16)); /* start memory test */
	  tmo = 0x1000000;
	  while( --tmo)
	  {
	    ctl = in_be32(  (volatile unsigned int *)0xfe008d00);
	    if( !(ctl & 0x80000000))
	    {
	      break;
	    }
	  }
	  if( !tmo)
	  {
	    printf(" -> timeout %08x\n", ctl);
	  }
	  else if( ctl & 1)
	  {
	    int data0, data1;
	    int attr, addr;

	    data0 = in_be32(  (volatile unsigned int *)0xfe008e20);
	    data1 = in_be32(  (volatile unsigned int *)0xfe008e24);
	    attr = in_be32(  (volatile unsigned int *)0xfe008e4c);
	    addr = in_be32(  (volatile unsigned int *)0xfe008e50);
	    printf(" -> error %08x'%08x %08x'%08x\n", data0, data1, attr, addr);
	  }
	  else
	  {
	    printf(" -> Done OK\n");
	  }
	}
	printf("       ");
}
#endif
