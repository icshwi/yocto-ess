DESCRIPTION = "Silicon Labs si5346 Synthesizer Kernel Driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c61be6a5b34a25baf73291577206d50"

SRC_URI = "git://git@gitlab.esss.lu.se/rosselliot/i2c-si5346.git;branch=master;protocol=ssh \
           file://si5346.conf \
"

#Change these when updating
PV = "0.0.1"
SRCREV = "5bda3e53dd5a480d4535ea6c4225f9a8f873d7fe"

S = "${WORKDIR}/git"

inherit module

RPROVIDES_${PN} += "kernel-module-si5346"

KERNEL_MODULE_AUTOLOAD += "si5346 "
#
#FILES_${PN} += "${sysconfdir}/modules-load.d/si5346.conf"
#                                                                                                                   
#do_install_append () {
#	install -d ${D}${sysconfdir}/modules-load.d
#	install -m 0644 ${WORKDIR}/si5346.conf ${D}${sysconfdir}/modules-load.d/si5346.conf
#}
