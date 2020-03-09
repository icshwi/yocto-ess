DESCRIPTION = "Silicon Labs si5346 Synthesizer Kernel Driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c61be6a5b34a25baf73291577206d50"

SRC_URI = "git://git@gitlab.esss.lu.se/rosselliot/i2c-si5346.git;branch=master;protocol=ssh \
           file://si5346.conf \
"

#Change these when updating
PV = "0.0.1"
SRCREV = "d3932960da4da3ec73bf9af19f0d7a90a76567f6"

S = "${WORKDIR}/git"

inherit module

RPROVIDES_${PN} += "kernel-module-si5346"

KERNEL_MODULE_AUTOLOAD += "si5346 "
