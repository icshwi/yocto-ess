FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-qoriq-2018.03:"
SRC_URI_append += "file://0001-Add-support-for-IFC14XX-board.patch \
                   file://ifc14xx/ \
                   file://ifc14xx_config.h \
                   file://IFC14XX_defconfig \
"

do_configure () {
    cp -r ${WORKDIR}/ifc14xx               ${S}/board/freescale/ifc14xx
    cp    ${WORKDIR}/ifc14xx_config.h      ${S}/include/configs/ifc14xx.h
    cp    ${WORKDIR}/IFC14XX_defconfig     ${S}/configs/IFC14XX_defconfig
}
