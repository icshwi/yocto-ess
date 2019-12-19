FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append := " file://zynq-picozed.dts "

do_compile_prepend() {
	install -m 0644 ${WORKDIR}/zynq-picozed.dts ${WORKDIR}/git/arch/arm/dts/
}

do_configure_prepend() {
	bbnote "Turn off direct OS boot from SPL and add support for SPI flash"
	sed -i -e 's,CONFIG_SPL_OS_BOOT=y,# CONFIG_SPL_OS_BOOT is not set\nCONFIG_ZYNQ_QSPI=y\nCONFIG_CMD_SF=y\nCONFIG_SPI_FLASH=y\nCONFIG_SPI_FLASH_BAR=y\nCONFIG_SPI_FLASH_MACRONIX=y\nCONFIG_SPI_FLASH_SPANSION=y\nCONFIG_SPI_FLASH_STMICRO=y\nCONFIG_SPI_FLASH_WINBOND=y\nCONFIG_CMD_SF=y\nCONFIG_ZYNQ_QSPI=y,g' ${WORKDIR}/git/configs/zynq_picozed_defconfig
	bbnote "Turn off USB support"
	sed -i -e 's,CONFIG_CMD_USB=y,# CONFIG_CMD_USB is not set,g' ${WORKDIR}/git/configs/zynq_picozed_defconfig
	sed -i -e 's,CONFIG_USB=y,# CONFIG_USB is not set,g' ${WORKDIR}/git/configs/zynq_picozed_defconfig
	sed -i -e 's,CONFIG_CMD_DFU=y,# CONFIG_CMD_DFU is not set,g' ${WORKDIR}/git/configs/zynq_picozed_defconfig
	sed -i -e 's,CONFIG_CMD_THOR_DOWNLOAD=y,# CONFIG_CMD_THOR_DOWNLOAD is not set,g' ${WORKDIR}/git/configs/zynq_picozed_defconfig

	bbnote "Add u-boot extra environment params"
	#sed -i 's/fit_image=[a-zA-Z0-9_\-\.\+]*/fit_image=image.ub\\0/g' ${WORKDIR}/git/include/configs/zynq-common.h 
	sed -i  '/^\s*DFU_ALT_INFO \\.*/i "bootrescue=echo Booting **Rescue** FPGA-IOC Linux image from QSPI; sf probe 0;sf read 0x2080000 0x200000 0x400000;sf read 0x2000000 0x6f0000 0x10000;sf read 0x3000000 0x700000 0x800000;sleep 1;bootm 0x2080000 0x3000000 0x2000000 \\0" \\' ${WORKDIR}/git/include/configs/zynq-common.h 
	sed -i  '/^\s*DFU_ALT_INFO \\.*/i "bootcmd=echo Booting FPGA-IOC Linux image from MMC; mmc rescan;fatload mmc 0:1 0x2080000 uImage;fatload mmc 0:1 0x2000000 picozed-zynq7.dtb;sleep 1;bootm 0x2080000 - 0x2000000 \\0" \\' ${WORKDIR}/git/include/configs/zynq-common.h 
}


# do_deploy_append(){
#	bbnote "Finishing device-tree"
#	mv ${DEPLOYDIR}/${DTB_FILE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}-${KERNEL_DEVICETREE}
#}
