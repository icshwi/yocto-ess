DESCRIPTION = "Struck SIS8300-KU FPGA Kernel Driver"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=73f1eb20517c55bf9493b7dd6e480788"

SRC_URI = "git://git@gitlab.esss.lu.se/icshwi/kmod-sis8300;branch=master;protocol=ssh \
           file://0001-Add-LICENSE-file.patch \
           file://0001-Adapt-Makefile-for-Yocto-build.patch \
           file://0001-Upgrade-sources-for-kernel-4-14.patch \
           file://sis8300-ku.conf \
"

#Change these when updating
PV = "0.0.1"
SRCREV = "6d58eb3d6e4e985f5da8bab0929beccdb6d160cd"

S = "${WORKDIR}/git"

inherit module qoriq_build_64bit_kernel

RPROVIDES_${PN} += "kernel-module-sis8300-ku"

do_install_append () {
	install -d ${D}${sysconfdir}/modules-load.d
	install -m 0644 ${WORKDIR}/sis8300-ku.conf ${D}${sysconfdir}/modules-load.d/sis8300drv.conf
}
