DESCRIPTION = "Unidata network Common Data Form"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSES/BLOSC.txt;md5=b4f98009927ff5a93d8a137caae35b97"

SRC_URI = "git://github.com/Blosc/c-blosc.git;nobranch=1;protocol=https \
"
SRCREV = "e63775855294b50820ef44d1b157f4de1cc38d3e"

S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DBUILD_TESTS=OFF -DBUILD_BENCHMARKS=OFF"
inherit cmake

PACKAGES = "${PN} ${PN}-dev ${PN}-staticdev ${PN}-dbg"
PROVIDES = "${PN} ${PN}-dev ${PN}-staticdev ${PN}-dbg"

FILES_${PN} += "${includedir} ${libdir}/*.so.*"
FILES_${PN}-dev += "${includedir} ${libdir}/[a-z]*.so ${libdir}/pkgconfig"
FILES_${PN}-staticdev += "${includedir} ${libdir}/*.a"

# Install lib files into the correct directory (/usr/lib64)
# for ifc14xx
do_install_append_qoriq() {
    install -d ${D}${libdir}
    mv ${D}/usr/lib/* ${D}${libdir}
    rm -rf ${D}/usr/lib
}

