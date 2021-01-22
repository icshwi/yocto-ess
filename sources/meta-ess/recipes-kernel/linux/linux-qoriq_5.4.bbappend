FILESEXTRAPATHS_prepend := "${THISDIR}/linux-qoriq-5.4:"

SRC_URI_append += "file://ifc14xx_5_4_defconfig \
                   file://ifc14xx.dts \
                   file://ifc14xx-sdk.dts \
                   file://ifc14xx.dtsi \
                   file://ifc14xx-pre.dtsi \
                   file://0001-Set-CONFIG_FSL_IFC-default-y.patch \
                   file://0001-Add-support-for-SGMII-BASEX-PHY.patch \
                   file://0001-Remove-unused-ethernet-ports-from-dts.patch \
"

SCMVERSION = "n"
LOCALVERSION = "-ifc14xx"

do_merge_delta_config_prepend () {
    cp -a ${WORKDIR}/ifc14xx_5_4_defconfig  ${S}/arch/powerpc/configs/ifc14xx_defconfig
}

do_compile_prepend () {
    cp -a ${WORKDIR}/ifc14xx-pre.dtsi       ${S}/arch/powerpc/boot/dts/fsl/ifc14xx-pre.dtsi
    cp -a ${WORKDIR}/ifc14xx.dtsi           ${S}/arch/powerpc/boot/dts/fsl/ifc14xx.dtsi
    cp -a ${WORKDIR}/ifc14xx.dts            ${S}/arch/powerpc/boot/dts/fsl/ifc14xx.dts
    cp -a ${WORKDIR}/ifc14xx-sdk.dts        ${S}/arch/powerpc/boot/dts/fsl/ifc14xx-sdk.dts
}
