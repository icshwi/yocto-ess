DESCRIPTION = "Tosca FPGA kernel driver"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b9b7c84bed8ea6fab5a6e3ee89e6fe0e"

SRC_URI = "git://git@gitlab.esss.lu.se/ioxos/tsc;branch=master;protocol=ssh \
           file://0001-Adapt-Makefile-for-Yocto.patch \
           file://0001-Add-license-file.patch \
           file://tsc.conf"

#Change these when updating
PV = "3.5.2"
SRCREV = "8ac258da70eb6fe00ad0369b1bbb3bc1970ea5c0"

S = "${WORKDIR}/git/driver"

inherit module qoriq_build_64bit_kernel

RPROVIDES_${PN} += "kernel-module-tsc"

do_install_append () {
	install -d ${D}${sysconfdir}/modules-load.d
	install -m 0644 ${WORKDIR}/tsc.conf ${D}${sysconfdir}/modules-load.d
}

