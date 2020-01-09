SUMMARY = "Service to enable DHCP on FPGAIOC" 
DESCRIPTION = "Enables DHCP an all interfaces"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit systemd

SRC_URI = " file://20-dhcp-fpgaioc.network \
"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/20-dhcp-fpgaioc.network ${D}${sysconfdir}/systemd/network/20-dhcp.network
}

