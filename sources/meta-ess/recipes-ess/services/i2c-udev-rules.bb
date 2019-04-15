SUMMARY = "Set permission on i2c devices"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"  

SRC_URI = "file://99-i2c.rules"    

do_install[nostamp] = "1"
do_unpack[nostamp] = "1"    

do_install () {
    install -d ${D}/etc/udev/rules.d
    install -m 0666 ${WORKDIR}/99-i2c.rules        ${D}/etc/udev/rules.d/99-i2c.rules
}    

FILES_${PN} += " /etc/udev/rules.d/99-i2c.rules"    

PACKAGES = "${PN}"
PROVIDES = "i2c-udev-rules"

