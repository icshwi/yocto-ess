FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

SRC_URI += "file://fragments.scc"
KERNEL_FEATURES_append = " fragments.scc"


