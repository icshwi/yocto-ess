SUMMARY = "Concurrent CPU init script"
DESCRIPTION = "General init for Concurrent CPU"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit systemd

SRC_URI = " file://cct-init.sh \
            file://cct-init.service \
"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} = "${datadir}/cct"

do_install() {
    install -d ${D}${datadir}/cct
    install -m 0755 ${WORKDIR}/cct-init.sh ${D}${datadir}/cct
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/cct-init.service ${D}${systemd_system_unitdir}
    sed -i -e 's|@SCRIPTDIR@|${datadir}/cct|g' ${D}${systemd_system_unitdir}/cct-init.service
}

SYSTEMD_SERVICE_${PN} = "cct-init.service"
