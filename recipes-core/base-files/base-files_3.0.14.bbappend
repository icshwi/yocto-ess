FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://dot.bashrc \
            file://dot.profile \
"

do_install_append () {
    install -d ${D}/home
    install -d ${D}/home/root

    install -m 0755 ${WORKDIR}/dot.bashrc ${D}/home/root/.bashrc
    install -m 0755 ${WORKDIR}/dot.profile ${D}/home/root/.profile
}

