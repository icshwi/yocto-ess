DESCRIPTION = "Kernel module for Xilinx XDMA"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=441c1ecbc199a036abf37f3aa47c5f34"

SRC_URI = "git://git@gitlab.esss.lu.se/beam-diagnostics/fpga/dma_ip_drivers.git;branch=master;protocol=ssh;subpath=XDMA/linux-kernel;destsuffix=git \
	   file://Fix-Makefile.patch \
	   file://Add-Makefile.patch \
	   file://xdma.conf \
	   file://60-xdma.rules \
"

#Change these when updating
PV = "2020.2.2"
SRCREV = "f37c29a08484e11226004a713a806d748e9f53d0"

# Build number - increment on change
PR = "r0"

S = "${WORKDIR}/git"

inherit module

RPROVIDES_${PN} += "kernel-module-xdma-driver"
PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
PROVIDES = "${PN} ${PN}-dbg ${PN}-dev"

do_install_append () {
    # Create install directories
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}/etc/udev/rules.d

    # Install driver auto load conf file
    install -m 0644 ${WORKDIR}/xdma.conf ${D}${sysconfdir}/modules-load.d/xdma.conf

    # Install udev rules
    install -m 0666 ${WORKDIR}/60-xdma.rules ${D}/etc/udev/rules.d/60-xdma.rules
}

FILES_${PN} += "/etc/udev/rules.d"
