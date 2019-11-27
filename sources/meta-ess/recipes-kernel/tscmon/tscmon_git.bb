DESCRIPTION = "Tosca FPGA kernel driver tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

INSANE_SKIP_${PN} = "ldflags"
RDEPENDS_${PN} = " bash tsc"

SRC_URI = "git://github.com/icshwi/tsc.git;branch=master;protocol=https"

#Change these when updating
PV = "4.0.2"
SRCREV = "a2766d9324fa31305d003bef19107b0af34a4223"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}/usr/bin

    cp ${S}/TscMon ${D}/usr/bin/TscMon
    ln -sf TscMon ${D}/usr/bin/tscmon
}
