FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-qoriq-2018.03:"
SRC_URI_append += "file://0001-Add-support-for-IFC14XX-board.patch \
                   file://0001-Add-lc-command-to-save-string-in-lowercase.patch \
                   file://0001-support-for-CPU-at-1800MHz-ethernet-10GB-line-revers.patch \
                   file://0002-Add-support-for-Micron-Cypress.patch \
                   file://0001-Add-prom-cmd-whatmem.patch \
                   file://0001-Unselect-errata-A009942.-Does-not-apply-to-IFC14xx.patch \
                   file://ifc14xx/ \
                   file://ifc14xx_config.h \
                   file://IFC14XX_defconfig \
"

do_configure () {
    cp -r ${WORKDIR}/ifc14xx               ${S}/board/freescale/ifc14xx
    cp    ${WORKDIR}/ifc14xx_config.h      ${S}/include/configs/ifc14xx.h
    cp    ${WORKDIR}/IFC14XX_defconfig     ${S}/configs/IFC14XX_defconfig
}
