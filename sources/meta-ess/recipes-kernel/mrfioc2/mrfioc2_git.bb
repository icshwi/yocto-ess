DESCRIPTION = "mrfioc2 kernel driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://git@github.com/javicereijo/mrfioc2.git;branch=master;protocol=ssh \
           file://0001-Adapt-Makefile-for-Yocto-build.patch \
           file://mrfioc2.conf \
           file://99-uio.rules"
SRCREV = "b4a57281cb11c831aab242b32b00557b174a0506"

S = "${WORKDIR}/git/mrmShared/linux"

inherit module

RPROVIDES_${PN} += "kernel-module-mrf-${KERNEL_VERSION}"

do_install_append () {
	install -d ${D}${sysconfdir}/modules-load.d
	install -m 0644 ${WORKDIR}/mrfioc2.conf ${D}${sysconfdir}/modules-load.d/mrf.conf

	install -d ${D}/etc/udev/rules.d
	install -m 0666 ${WORKDIR}/99-uio.rules ${D}/etc/udev/rules.d/99-uio.rules
}
FILES_${PN} = "${sysconfdir}/modules-load.d /lib/modules/${KERNEL_VERSION}/extra /etc/udev/rules.d"
