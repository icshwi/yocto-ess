DESCRIPTION = "Tosca FPGA kernel driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/icshwi/tsc.git;branch=master;protocol=https \
           file://tsc.conf"

#Change these when updating
PV = "4.0.3"
SRCREV = "812dec50dde4526ed40dfff15750aaea4a3bb098"

S = "${WORKDIR}/git/driver"

inherit module qoriq_build_64bit_kernel

RPROVIDES_${PN} += "kernel-module-tsc"

do_install_append () {
	install -d ${D}${sysconfdir}/modules-load.d
	install -m 0644 ${WORKDIR}/tsc.conf ${D}${sysconfdir}/modules-load.d
}
