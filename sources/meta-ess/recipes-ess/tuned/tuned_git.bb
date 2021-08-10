DESCRIPTION = "Daemon for monitoring and adaptive tuning of system devices"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/redhat-performance/tuned.git;nobranch=1;protocol=https \
           file://0001-Remove-desktop-file-install-step-from-Makefile.patch \
          "


SRCREV = "5397166de564e124996e5b7e4e4371838b14d9b0"

S = "${WORKDIR}/git"
PV = "2.15.0"

# Increment revision number if package changes...
PR = "r1"

inherit setuptools3 systemd

RDEPENDS_${PN} = "bash \
                  python3-pip \
                  python3-core \
                  python3-pickle \
                  python3-decorator \
                  python3-dbus \
                  python3-pycairo \
                  python3-pygobject \
                  python3-pyudev \
                  systemtap \
                  virt-what"

PACKAGES = "${PN} ${PN}-dbg ${PN}-dev ${PN}-doc"
PROVIDES = "${PN} ${PN}-dbg ${PN}-dev ${PN}-doc"

# No configure ...
do_configure() {
}

# No compile ...
do_compile() {
}

# Just packaging in install
do_install() {
    make DESTDIR=${D} install
    install -d ${D}/usr/lib/python3.5/site-packages
    mv ${D}/usr/lib/python3/dist-packages/tuned ${D}/usr/lib/python3.5/site-packages/tuned
    rm -rf ${D}/usr/lib/python3

    # Install systemd service
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${D}/usr/lib/systemd/system/tuned.service ${D}${systemd_system_unitdir}
    rm -rf ${D}/usr/lib/systemd
}

FILES_${PN} += "/run /usr/share/polkit-1 /usr/share/bash-completion /usr/share/icons"
SYSTEMD_SERVICE_${PN} = "tuned.service"
