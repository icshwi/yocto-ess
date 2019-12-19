FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://fragments.scc"
KERNEL_FEATURES_append = " fragments.scc"


