FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_qoriq-ppc_append = "\
    file://0001-Fix-for-host-gcc-6.3.patch \
"
