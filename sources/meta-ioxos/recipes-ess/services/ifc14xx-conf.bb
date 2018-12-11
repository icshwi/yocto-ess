SUMMARY = "IFC14XX config to calibrate DDR and power on FMCs"
DESCRIPTION = "Calibrates both DDR banks and power up both FMCs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit systemd

SRC_URI = " file://ifc14xx-init.sh \
            file://ifc14xx-init.service \
"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} = "${datadir}/ifc14xx"

do_install() {
    install -d ${D}${datadir}/ifc14xx
    install -m 0755 ${WORKDIR}/ifc14xx-init.sh ${D}${datadir}/ifc14xx
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/ifc14xx-init.service ${D}${systemd_system_unitdir}
    sed -i -e 's|@SCRIPTDIR@|${datadir}/ifc14xx|g' ${D}${systemd_system_unitdir}/ifc14xx-init.service
}

SYSTEMD_SERVICE_${PN} = "ifc14xx-init.service"
