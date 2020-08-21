DESCRIPTION = "Silicon Labs si5332 Synthesizer Kernel Driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c61be6a5b34a25baf73291577206d50"

SRC_URI = "git://git@gitlab.esss.lu.se/rosselliot/i2c-si5332.git;branch=master;protocol=ssh \
           file://si5332.conf \
"

#Change these when updating
PV = "0.0.1"
SRCREV = "e991f6b4eb561067debd216fe854a027dcc072d1"
S = "${WORKDIR}/git"

inherit module

RPROVIDES_${PN} += "kernel-module-si5332"

KERNEL_MODULE_AUTOLOAD += "si5332"
