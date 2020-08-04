DESCRIPTION = "Silicon Labs si5332 Synthesizer Kernel Driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c61be6a5b34a25baf73291577206d50"

SRC_URI = "git://git@gitlab.esss.lu.se/rosselliot/i2c-si5332.git;branch=master;protocol=ssh \
           file://si5332.conf \
"

#Change these when updating
PV = "0.0.1"
SRCREV = "fcb0fa801f227c897c1002c265b39a4d8e370c91"
S = "${WORKDIR}/git"

inherit module

RPROVIDES_${PN} += "kernel-module-si5332"

KERNEL_MODULE_AUTOLOAD += "si5353"
