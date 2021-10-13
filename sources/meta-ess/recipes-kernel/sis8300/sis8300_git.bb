DESCRIPTION = "Struck SIS8300-KU FPGA Kernel Driver"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=73f1eb20517c55bf9493b7dd6e480788"

SRC_URI = "git://git@gitlab.esss.lu.se/epics-modules/sis8300drv.git;branch=master;protocol=ssh \
           file://0001-Adapt-Makefile-for-Yocto-build.patch \
           file://0001-Upgrade-sources-for-kernel-4-14.patch \
           file://0001-Adapt-lib-Makefile-for-cross-compilation.patch \
           file://sis8300-ku.conf \
           file://99-sis8300.rules \
"

#Change these when updating
PV = "4.10.2"
SRCREV = "d2d85a425b72c839a764af1d924d7f16411c8b33"

S = "${WORKDIR}/git"

# Build number - increment on change
PR = "r0"

inherit module

RPROVIDES_${PN} += "kernel-module-sis8300-ku"
PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
PROVIDES = "${PN} ${PN}-dbg ${PN}-dev"

MODULES_MODULE_SYMVERS_LOCATION = "src/main/c/driver"

# Prevent do_configure from running clean step
CLEANBROKEN = "1"

do_compile_prepend() {
    cd src/main/c/driver
}

do_install_prepend () {
    cd src/main/c/driver
}

do_install_append () {
    # Create install directories
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}/etc/udev/rules.d

    # Install driver auto load conf file
    install -m 0644 ${WORKDIR}/sis8300-ku.conf ${D}${sysconfdir}/modules-load.d/sis8300drv.conf

    # Install udev rules
    install -m 0666 ${WORKDIR}/99-sis8300.rules ${D}/etc/udev/rules.d/99-sis8300.rules
}

FILES_${PN} += "/etc/udev/rules.d"
