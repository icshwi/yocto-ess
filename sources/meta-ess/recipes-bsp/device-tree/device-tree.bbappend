FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit xilinx-platform-init

KERNEL_DTS_INCLUDE = "${THISDIR}/files"

SRC_URI_append_picozed-zynq7 += " \
                file://picozed-zynq7.dts \
		"

KERNEL_DTS_INCLUDE_append_picozed-zynq7 = " \
		${STAGING_KERNEL_DIR}/arch/${ARCH}/boot/dts/ \
		"
