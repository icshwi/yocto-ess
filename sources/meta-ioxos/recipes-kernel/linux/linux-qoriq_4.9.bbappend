FILESEXTRAPATHS_prepend := "${THISDIR}/linux-qoriq-4.9:"

SRCREV = "e8b01fb24fb8eb1adee9667eba2cae702b5892e9"

SRC_URI_append += "file://ifc1410_4_9_defconfig \
                   file://ifc1410.dts \
                   file://ifc1410-sdk.dts \
                   file://ifc1410.dtsi \
                   file://ifc1410-pre.dtsi \
                   file://0001-Set-CONFIG_FSL_IFC-default-y.patch \
                   file://0001-Configure-IFC1410-SGMII-PHY.patch \
                   file://0001-Fixup-in-phy.h.patch \
                   file://0001-Use-phy-connection.patch \
                   file://0002-ver_linux-Use-usr-bin-awk-instead-of-bin-awk.patch \
                   file://0001-Remove-unused-ethernet-ports-from-dts.patch \
"

SCMVERSION = "n"
LOCALVERSION = "-ess"

do_merge_delta_config_prepend () {
    cp -a ${WORKDIR}/ifc1410_4_9_defconfig  ${S}/arch/powerpc/configs/ifc1410_defconfig
}

do_compile_prepend () {
    cp -a ${WORKDIR}/ifc1410-pre.dtsi       ${S}/arch/powerpc/boot/dts/fsl/ifc1410-pre.dtsi
    cp -a ${WORKDIR}/ifc1410.dtsi           ${S}/arch/powerpc/boot/dts/fsl/ifc1410.dtsi
    cp -a ${WORKDIR}/ifc1410.dts            ${S}/arch/powerpc/boot/dts/fsl/ifc1410.dts
    cp -a ${WORKDIR}/ifc1410-sdk.dts        ${S}/arch/powerpc/boot/dts/fsl/ifc1410-sdk.dts
}
