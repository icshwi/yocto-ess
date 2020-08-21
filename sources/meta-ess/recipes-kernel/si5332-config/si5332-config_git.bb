DESCRIPTION = "Helper utility to communicate with Silicon Labs si5332 Synthesizer Kernel Driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=7c61be6a5b34a25baf73291577206d50"

SRC_URI = "git://git@gitlab.esss.lu.se/rosselliot/i2c-si5332.git;branch=master;protocol=ssh \
"

#Change these when updating
PV = "0.0.1"
SRCREV = "e991f6b4eb561067debd216fe854a027dcc072d1"
S = "${WORKDIR}/git/si5332_config"

do_install() {
    install -d ${D}/usr/bin

    cp ${S}/si5332_config ${D}/usr/bin/si5332_config
}
