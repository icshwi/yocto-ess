DESCRIPTION = "Tosca FPGA kernel driver tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

INSANE_SKIP_${PN} = "ldflags"
RDEPENDS_${PN} = " bash tsc"
SECURITY_STACK_PROTECTOR = ""

SRC_URI = "git://github.com/icshwi/tsc.git;branch=master;protocol=https \
           file://0001-Update-TscMon-for-GCC-10.patch \
          "
#Change these when updating
PV = "4.0.8"
SRCREV = "299f7d5f2ee68d0cd085edb26c9fe4e09dcc9198"
# Increment revision number if package changes...
PR = "r0"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}/usr/bin

    cp ${S}/TscMon ${D}/usr/bin/TscMon
    ln -sf TscMon ${D}/usr/bin/tscmon
}
