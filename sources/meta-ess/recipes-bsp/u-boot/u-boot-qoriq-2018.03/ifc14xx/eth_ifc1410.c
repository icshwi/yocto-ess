/*
 * Copyright 2014 Freescale Semiconductor, Inc.
 *
 * Shengzhou Liu <Shengzhou.Liu@freescale.com>
 *
 * SPDX-License-Identifier:     GPL-2.0+
 */

#include <common.h>
#include <command.h>
#include <netdev.h>
#include <asm/mmu.h>
#include <asm/processor.h>
#include <asm/immap_85xx.h>
#include <asm/fsl_law.h>
#include <asm/fsl_serdes.h>
#include <asm/fsl_portals.h>
#include <asm/fsl_liodn.h>
#include <malloc.h>
#include <fm_eth.h>
#include <fsl_mdio.h>
#include <miiphy.h>
#include <phy.h>
#include <fsl_dtsec.h>
#include <asm/fsl_serdes.h>
#include <micrel.h>

#include "board_id.h"

#define CONFIG_IFC1410_DTSECx

int board_eth_init(bd_t *bis)
{
#if defined(CONFIG_IFC1410_DTSECx)
	const char	*devname;
	int retval;
	ushort reg;
#endif
	int board_type = *(int *)CONFIG_SYS_PON_BASE & 0xffff;

	if( board_type == BOARD_TYPE_IFC1410) {
		//*(volatile uint *)0xfe0eA664 = 0xf0000800;
		//*(volatile uint *)0xfe0eA674 = 0xe8000800;
		*(volatile uint *)0xfe0eA800 = 0x33051500;
		*(volatile uint *)0xfe0eA804 = 0xc0000011;
		*(volatile uint *)0xfe0eA800 = 0x33651500;
		*(volatile uint *)0xfe0eA840 = 0x33051500;
		*(volatile uint *)0xfe0eA844 = 0xc0000011;
		*(volatile uint *)0xfe0eA840 = 0x33651500;
	}

#if defined(CONFIG_FMAN_ENET)
	int i, interface;
	struct memac_mdio_info dtsec_mdio_info;
	struct memac_mdio_info tgec_mdio_info;
	struct mii_dev *dev;
	ccsr_gur_t *gur = (void *)(CONFIG_SYS_MPC85xx_GUTS_ADDR);
	u32 srds_s1;

#if defined(CONFIG_IFC1410_DTSECx)
	struct memac_mdio_info dtsec1_mdio_info;
	struct memac_mdio_info dtsec2_mdio_info;
#endif

	srds_s1 = in_be32(&gur->rcwsr[4]) &
					FSL_CORENET2_RCWSR4_SRDS1_PRTCL;
	srds_s1 >>= FSL_CORENET2_RCWSR4_SRDS1_PRTCL_SHIFT;

	dtsec_mdio_info.regs =
		(struct memac_mdio_controller *)CONFIG_SYS_FM1_DTSEC_MDIO_ADDR;

	dtsec_mdio_info.name = DEFAULT_FM_MDIO_NAME;

	/* Register the 1G MDIO bus */
	fm_memac_mdio_init(bis, &dtsec_mdio_info);

	tgec_mdio_info.regs =
		(struct memac_mdio_controller *)CONFIG_SYS_FM1_TGEC_MDIO_ADDR;
	tgec_mdio_info.name = DEFAULT_FM_TGEC_MDIO_NAME;

	/* Register the 10G MDIO bus */
	fm_memac_mdio_init(bis, &tgec_mdio_info);

	/* Set the two on-board RGMII PHY address */
	fm_info_set_phy_address(FM1_DTSEC3, RGMII_PHY1_ADDR);

	switch (srds_s1) {
	case 0x66:
	case 0x6b:
		fm_info_set_phy_address(FM1_10GEC1, CORTINA_PHY_ADDR1);
		fm_info_set_phy_address(FM1_10GEC2, CORTINA_PHY_ADDR2);
		fm_info_set_phy_address(FM1_10GEC3, FM1_10GEC3_PHY_ADDR);
		fm_info_set_phy_address(FM1_10GEC4, FM1_10GEC4_PHY_ADDR);
		break;
	case 0x6c: /* IFC1410 -> XFI_9 + XFI_10 + SGMII_1 + SGMII_2 + PCIe_4(Gen2) */
	case 0xbc: /* IFC1410 -> PCIe_3(Gen2) + SGMII_1 + SGMII_2 + PCIe_4(Gen3)   */
	  //fm_info_set_phy_address(FM1_DTSEC1, 0);
	  //fm_info_set_phy_address(FM1_DTSEC2, SGMII_CARD_PORT3_PHY_ADDR);
		break;
	case 0xaa: /* IFC1211 */
		break;
	default:
		printf("SerDes1 protocol 0x%x is not supported on T208xRDB\n",
		       srds_s1);
		break;
	}

	for (i = FM1_DTSEC1; i < FM1_DTSEC1 + CONFIG_SYS_NUM_FM1_DTSEC; i++) {
		interface = fm_info_get_enet_if(i);
		switch (interface) {
		case PHY_INTERFACE_MODE_RGMII:
			dev = miiphy_get_dev_by_name(DEFAULT_FM_MDIO_NAME);
			fm_info_set_mdio(i, dev);
			break;
		default:
			break;
		}
	}

	for (i = FM1_10GEC1; i < FM1_10GEC1 + CONFIG_SYS_NUM_FM1_10GEC; i++) {
		switch (fm_info_get_enet_if(i)) {
		case PHY_INTERFACE_MODE_XGMII:
			dev = miiphy_get_dev_by_name(DEFAULT_FM_TGEC_MDIO_NAME);
			fm_info_set_mdio(i, dev);
			break;
		default:
			break;
		}
	}

	cpu_eth_init(bis);
#endif /* CONFIG_FMAN_ENET */

#if defined(CONFIG_IFC1410_DTSECx)
	retval = pci_eth_init(bis);


	/* use current device */
	devname = miiphy_get_current_dev();


        retval = miiphy_read(devname, 3, MII_CTRL1000, &reg);
	miiphy_write (devname, 3, 0xd, 2);
	miiphy_write (devname, 3, 0xe, 8);
	miiphy_write (devname, 3, 0xd, 0x4002);
	miiphy_write (devname, 3, 0xe, 0x3e6f);

	printf("leaving board_eth_init()\n");
	return(retval);
#else
	return pci_eth_init(bis);
#endif
}

void fdt_fixup_board_enet(void *fdt)
{
	return;
}
