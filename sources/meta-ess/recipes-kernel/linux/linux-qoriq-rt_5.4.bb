inherit kernel qoriq_build_64bit_kernel siteinfo
inherit fsl-kernel-localversion

SUMMARY = "Linux Kernel for NXP QorIQ platforms"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

#file://0001-fix-gcc8-build-error.patch 
#file://0001-powerpc-disable-attribute-alias-warning.patch 
#file://0001-powerpc-fix-stringop-truncation-error.patch 
SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/linux;nobranch=1 \
    file://ifc14xx_rt_5_4_defconfig \
    file://ifc14xx.dts \
    file://ifc14xx-sdk.dts \
    file://ifc14xx.dtsi \
    file://ifc14xx-pre.dtsi \
    file://0001-Set-CONFIG_FSL_IFC-default-y.patch \
    file://0001-Add-support-for-SGMII-BASEX-PHY.patch \
    file://0001-Remove-unused-ethernet-ports-from-dts.patch \
"
SRCREV = "134788b16485dd9fa81988681d2365ee38633fa2"

S = "${WORKDIR}/git"

DEPENDS_append = " libgcc"
# not put Images into /boot of rootfs, install kernel-image if needed
RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""

KERNEL_CC_append = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append = " ${TOOLCHAIN_OPTIONS}"
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

ZIMAGE_BASE_NAME = "zImage-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
ZIMAGE_BASE_NAME[vardepsexclude] = "DATETIME"

SCMVERSION = "n"
LOCALVERSION = "-ifc14xx"
DELTA_KERNEL_DEFCONFIG ?= ""

do_merge_delta_config[dirs] = "${B}"

do_merge_delta_config_prepend () {
    cp -a ${WORKDIR}/ifc14xx_rt_5_4_defconfig  ${S}/arch/powerpc/configs/ifc14xx_rt_defconfig
}

do_merge_delta_config() {
    # create config with make config
    oe_runmake  -C ${S} O=${B} ${KERNEL_DEFCONFIG}
    
    # check if bigendian is enabled
    if [ "${SITEINFO_ENDIANNESS}" = "be" ]; then
        echo "CONFIG_CPU_BIG_ENDIAN=y" >> .config
        echo "CONFIG_MTD_CFI_BE_BYTE_SWAP=y" >> .config
    fi

    # add config fragments
    for deltacfg in ${DELTA_KERNEL_DEFCONFIG}; do
        if [ -f ${S}/arch/${ARCH}/configs/${deltacfg} ]; then
            oe_runmake  -C ${S} O=${B} ${deltacfg}
        elif [ -f "${WORKDIR}/${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${WORKDIR}/${deltacfg}
        elif [ -f "${deltacfg}" ]; then
            ${S}/scripts/kconfig/merge_config.sh -m .config ${deltacfg}
        fi
    done
    cp .config ${WORKDIR}/defconfig
}
addtask merge_delta_config before do_preconfigure after do_patch

do_compile_prepend () {
    cp -a ${WORKDIR}/ifc14xx-pre.dtsi       ${S}/arch/powerpc/boot/dts/fsl/ifc14xx-pre.dtsi
    cp -a ${WORKDIR}/ifc14xx.dtsi           ${S}/arch/powerpc/boot/dts/fsl/ifc14xx.dtsi
    cp -a ${WORKDIR}/ifc14xx.dts            ${S}/arch/powerpc/boot/dts/fsl/ifc14xx.dts
    cp -a ${WORKDIR}/ifc14xx-sdk.dts        ${S}/arch/powerpc/boot/dts/fsl/ifc14xx-sdk.dts
}

FILES_${KERNEL_PACKAGE_NAME}-image += "/boot/zImage*"
COMPATIBLE_MACHINE = "(qoriq)"
