DESCRIPTION = "procServ wrapper to start arbitrary interactive commands in the background, with telnet access to stdin/stdout"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "automake autoconf"

SRC_URI = "git://github.com/ralphlange/procServ.git;branch=master;protocol=https \
"
SRCREV = "2eab20f7f80ed1aaaeb46c7bbdcc0db09116a2e3"

S = "${WORKDIR}/git"

do_compile() {
    make
    ./configure --disable-doc --enable-access-from-anywhere --host=ppc64
    make
}

do_install() {
    install -d ${D}/usr/bin

    cp ${S}/procServ ${D}/usr/bin/procServ
}

