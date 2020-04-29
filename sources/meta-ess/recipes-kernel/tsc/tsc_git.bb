DESCRIPTION = "Tosca FPGA kernel driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/icshwi/tsc.git;branch=master;protocol=https \
           file://tsc.conf"

#Change these when updating
PV = "4.0.6"
SRCREV = "2d20ac283fcb8929bf89715d05c73d0a1c48b36a"

S = "${WORKDIR}/git/driver"

inherit module qoriq_build_64bit_kernel

RPROVIDES_${PN} += "kernel-module-tsc"

do_install_append () {
	install -d ${D}${sysconfdir}/modules-load.d
	install -m 0644 ${WORKDIR}/tsc.conf ${D}${sysconfdir}/modules-load.d
}
