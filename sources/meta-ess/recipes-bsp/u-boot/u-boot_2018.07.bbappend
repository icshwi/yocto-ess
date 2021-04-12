FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-2018.07:"

SRC_URI_append := " file://zynq-picozed.dts \
                    file://0001-Modify-defconfig-for-FPGAIOC.patch \
                    file://0001-Add-u-boot-commands-for-FPGAIOC.patch \
                    file://0001-SPI-CMD-Permit-Setting-Bus-Frequency.patch \
                  "

do_compile_prepend() {
	install -m 0644 ${WORKDIR}/zynq-picozed.dts ${WORKDIR}/git/arch/arm/dts/
}
