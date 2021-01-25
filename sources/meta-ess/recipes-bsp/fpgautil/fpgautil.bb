SUMMARY = "Xilinx user script to support fpga-manager"
DESCRIPTION = "Xilinx user script that loads full/partial bitstreams using the kernel fpga-manager"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${WORKDIR}/fpgautil.c;beginline=1;endline=24;md5=8010e59a286b1e3a73a9fdd93bd18778"

SRC_URI = "https://raw.githubusercontent.com/Xilinx/meta-xilinx-tools/6235e09392d268a588791d22c05f81a45260312c/recipes-bsp/fpga-manager-script/files/fpgautil.c"
SRC_URI[md5sum] = "30b0293b6deb7734bab972d4ff070565"

S = "${WORKDIR}"

do_compile() {
	${CC} ${LDFLAGS} fpgautil.c -o fpgautil
}

do_install() {
        install -Dm 0755 ${S}/fpgautil ${D}${bindir}/fpgautil
}

FILES_${PN} = "\
        ${bindir}/fpgautil \
        "
