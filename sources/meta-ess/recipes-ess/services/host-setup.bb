SUMMARY = "Setup host for ESS"
DESCRIPTION = "Setup host for technical network and E3"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit systemd

SRC_URI = " file://host-init.sh \
            file://host-setup.service \
"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} = "${datadir}/host-init"

do_install() {
    install -d ${D}${datadir}/host-init
    install -m 0755 ${WORKDIR}/host-init.sh ${D}${datadir}/host-init
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/host-setup.service ${D}${systemd_system_unitdir}
    sed -i -e 's|@SCRIPTDIR@|${datadir}/host-init|g' ${D}${systemd_system_unitdir}/host-setup.service
}

SYSTEMD_SERVICE_${PN} = "host-setup.service"

