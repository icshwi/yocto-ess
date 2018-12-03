FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-qoriq-2017.12:"
SRC_URI_append += "file://0001-Add-support-for-IOxOS-IFC1410.patch \
                   file://ifc1410/ \
                   file://ifc1410_config.h \
                   file://IFC1410_defconfig \
"

do_configure () {
    cp -r ${WORKDIR}/ifc1410               ${S}/board/freescale/ifc1410
    cp    ${WORKDIR}/ifc1410_config.h      ${S}/include/configs/ifc1410.h
    cp    ${WORKDIR}/IFC1410_defconfig     ${S}/configs/IFC1410_defconfig
}
