FILESEXTRAPATHS_prepend := "${THISDIR}/linux-qoriq-4.9:"

SRC_URI_append += "file://0001-Revert-driver-mtd-ifc-increase-eccstat-array-size-fo.patch \
                   file://0002-Revert-driver-mtd-ifc-update-bufnum-mask-for-ver-2.0.patch \
                   file://0003-Revert-driver-mtd-ifc-Initialize-SRAM-for-all-versio.patch \
                   file://0002-ver_linux-Use-usr-bin-awk-instead-of-bin-awk.patch \
                   file://ifc1410_4_9_defconfig \
                   file://ifc1410.dts \
                   file://ifc1410.dtsi \
                   file://ifc1410-pre.dtsi \
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
}
