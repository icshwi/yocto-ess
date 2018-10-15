#
# Copyright 2014 Freescale Semiconductor, Inc.
#
# SPDX-License-Identifier:      GPL-2.0+
#

ifdef CONFIG_SPL_BUILD
obj-y += spl.o
else
obj-$(CONFIG_IFC1410) += ifc1410.o
obj-$(CONFIG_IFC1410) += eth_ifc1410.o
obj-$(CONFIG_PCI)     += pci.o
endif

obj-y   += ddr.o
obj-y   += law.o
obj-y   += tlb.o
obj-y   += conf.o
obj-y   += fpga.o
