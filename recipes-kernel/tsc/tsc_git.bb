DESCRIPTION = "Tosca FPGA kernel driver"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b9b7c84bed8ea6fab5a6e3ee89e6fe0e"

SRC_URI = "git://git@gitlab.esss.lu.se/ioxos/tsc;branch=master;protocol=ssh \
           file://0001-Adapt-Makefile-for-Yocto.patch \
           file://0001-Add-license-file.patch"
SRCREV = "27901e5c8b1e5271227ff60784ebcc3ed13c6724"

S = "${WORKDIR}/git/driver"

inherit module qoriq_build_64bit_kernel

RPROVIDES_${PN} += "kernel-module-tsc"

