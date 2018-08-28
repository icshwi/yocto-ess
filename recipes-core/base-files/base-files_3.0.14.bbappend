FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://dot.bashrc \
            file://dot.profile \
"

do_install_append () {
    install -d ${D}/home
    install -d ${D}/home/root
    install -d ${D}/epics

    install -m 0755 ${WORKDIR}/dot.bashrc ${D}/home/root/.bashrc
    install -m 0755 ${WORKDIR}/dot.profile ${D}/home/root/.profile

    cat >> ${D}${sysconfdir}/fstab <<EOF
# Generated from yocto
10.4.0.144:/opt/epics_e3_2.5 /epics          nfs        defaults              0  0

EOF
}

