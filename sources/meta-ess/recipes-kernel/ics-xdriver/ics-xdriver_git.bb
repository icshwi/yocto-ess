DESCRIPTION = "Kernel module for the LLRF RTM carrier AMC"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=441c1ecbc199a036abf37f3aa47c5f34"

SRC_URI = "git://git@gitlab.esss.lu.se/icshwi/ics-xdriver-core.git;branch=master;protocol=ssh \
           file://0001-Modify-Makefile-for-Yocto.patch \
           file://0001-Add-Xilinx-BSD-LICENSE.patch \
           file://xdma.conf \
"

#Change these when updating
PV = "2017.1.48"
SRCREV = "123088a9bd1d989f0b1239f6f8e411392bafab3d"

S = "${WORKDIR}/git"

# Build number - increment on change
PR = "r0"

inherit module

RPROVIDES_${PN} += "kernel-module-ics-xdriver"
PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
PROVIDES = "${PN} ${PN}-dbg ${PN}-dev"

do_install_append () {
    # Create install directories
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}/etc/udev/rules.d

    # Install driver auto load conf file
    install -m 0644 ${WORKDIR}/xdma.conf ${D}${sysconfdir}/modules-load.d/xdma.conf

    # Install udev rules
    install -m 0666 ${S}/60-xdma.rules ${D}/etc/udev/rules.d/60-xdma.rules
}

FILES_${PN} += "/etc/udev/rules.d"
