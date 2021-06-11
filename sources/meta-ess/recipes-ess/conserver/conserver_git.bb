DESCRIPTION = "Conserver provides remote access to serial port \
consoles and logs all data to a central host."
HOMEPAGE = "http://www.conserver.com/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b28513e7b696027d3d2b8dbf117f9fe5"

SRC_URI = "git://git@github.com/bstansell/conserver.git;protocol=http;branch=master \
           file://console.cf \
           file://conserver.cf \
           file://conserver.passwd \
           file://conserver.service \
           file://console-autocomp.bash"
SRCREV = "c8355ae8b9691f02e9b76fe74397b2fa9d8c3562"

PV = "8.2.6"
PR = "r1"

S = "${WORKDIR}/git"

# Use autotools-brokensep to prevent separation of source and build dirs, i.e.,
# build in the source dir...
inherit autotools-brokensep systemd

# Requires libcrypt for linking. Use extended crypt library package
DEPENDS += "libxcrypt"

# Runtime dependency on bash-completion
RDEPENDS_${PN} += "bash-completion"

# Stop install being called with -s so it doesn't try and run the host strip command
HOSTTOOLS = "install"
EXTRA_OEMAKE = "INSTALL_PROGRAM=install"

# Compile with the --with-uds option to enable Unix domain sockets for client/server
# communication
EXTRA_OECONF += " --with-uds"

# Packages
PACKAGES = "${PN} ${PN}-dbg ${PN}-doc"
PROVIDES = "${PN} ${PN}-dbg ${PN}-doc"

# Package doc files
FILES_${PN}-doc += "/usr/share"

# Install default configuration files
do_install_append() {
    # Create directories
    install -d ${D}/etc
    install -d ${D}/etc/bash_completion.d
    install -d ${D}${systemd_system_unitdir}
    # Install sources
    install -m 0644 ${WORKDIR}/console.cf ${D}/etc/
    install -m 0644 ${WORKDIR}/conserver.cf ${D}/etc/
    install -m 0644 ${WORKDIR}/conserver.passwd ${D}/etc/
    install -m 0644 ${WORKDIR}/console-autocomp.bash ${D}/etc/bash_completion.d/
    install -m 0644 ${WORKDIR}/conserver.service ${D}${systemd_system_unitdir}
}

SYSTEMD_SERVICE_${PN} = "conserver.service"
PACKAGE_ARCH = "${MACHINE_ARCH}"
