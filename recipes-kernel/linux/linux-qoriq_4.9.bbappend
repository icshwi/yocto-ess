FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append += "file://0001-Revert-driver-mtd-ifc-increase-eccstat-array-size-fo.patch \
                   file://0002-Revert-driver-mtd-ifc-update-bufnum-mask-for-ver-2.0.patch \
                   file://0003-Revert-driver-mtd-ifc-Initialize-SRAM-for-all-versio.patch \
                   file://0001-Add-support-for-IOxOS-IFC1410.patch \
                   file://0002-ver_linux-Use-usr-bin-awk-instead-of-bin-awk.patch \
"

SCMVERSION = "n"
LOCALVERSION = "-ess"

