DESCRIPTION = "Tosca FPGA kernel driver tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

INSANE_SKIP_${PN} = "ldflags"
RDEPENDS_${PN} = " bash tsc"

SRC_URI = "git://github.com/icshwi/tsc.git;branch=master;protocol=https"

#Change these when updating
PV = "4.0.4"
SRCREV = "6e53f71c62e65ffca8aa6280db00b91cd6b4e9e0"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}/usr/bin

    cp ${S}/TscMon ${D}/usr/bin/TscMon
    ln -sf TscMon ${D}/usr/bin/tscmon
}
