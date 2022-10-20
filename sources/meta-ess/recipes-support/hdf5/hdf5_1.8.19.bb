LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=57e5351b17591e659eedae107265c606"

BBCLASSEXTEND = "native"

SRC_URI = "http://www.hdfgroup.org/ftp/HDF5/releases/hdf5-1.8/hdf5-1.8.19/src/hdf5-1.8.19.tar.bz2"

# automake is stricter on quoting now
SRC_URI += "file://${PV}/0001-hdf5-am-quote.patch"

# switch the threading off
SRC_URI += "file://${PV}/0003-hdf5-threads.patch"

# force shared library build
SRC_URI += "file://${PV}/0004-hdf5-shared.patch"

# avoid generating of the H5Tinit.c and H5lib_settings.c files
SRC_URI += "file://${PV}/0002-hdf5-c.patch"

# H5Tinit.c and H5lib_settings.c files for each platform
SRC_URI += "${H5_CONFIG_FILES}"

H5_CONFIG_FILES ?= " \
"

H5_CONFIG_FILES_qoriq = " \
    file://${PV}/qoriq/H5Tinit.c \
    file://${PV}/qoriq/H5lib_settings.c \
"

H5_CONFIG_FILES_intel-x86-common = " \
    file://${PV}/cct/H5Tinit.c \
    file://${PV}/cct/H5lib_settings.c \
"

SRC_URI[md5sum] = "6f0353ee33e99089c110a1c8d2dd1b22"
SRC_URI[sha256sum] = "59c03816105d57990329537ad1049ba22c2b8afe1890085f0c022b75f1727238"

DEPENDS += "zlib"
S = "${WORKDIR}/hdf5-1.8.19"
# Increment revision number if package changes...
PR = "r2"

EXTRA_OECONF = "--with-szlib=no --with-pthread=no --enable-threadsafe=no --enable-cxx=yes"
EXTRA_OECONF += "hdf5_cv_fp_to_integer_overflow_works=yes"
EXTRA_OECONF += "hdf5_cv_ldouble_to_long_special=no"
EXTRA_OECONF += "hdf5_cv_long_to_ldouble_special=no"
EXTRA_OECONF += "hdf5_cv_ldouble_to_llong_accurate=yes"
EXTRA_OECONF += "hdf5_cv_llong_to_ldouble_correct=yes"

PROVIDES += "${PN}-tests ${PN}-tests-dbg ${PN}-examples"
PACKAGES += "${PN}-tests ${PN}-tests-dbg ${PN}-examples"

FILES_${PN} += "${libdir}/libhdf5.settings"
FILES_${PN}-tests = "${libdir}/hdf5-tests/[a-z]*"
FILES_${PN}-tests-dbg = "${libdir}/hdf5-tests/.debug"
FILES_${PN}-examples = "${datadir}/hdf5_examples"

inherit autotools pkgconfig

# add files generated by HDF5 during compilation; these cannot be
# generated during cross-compilation
do_configure_append_qoriq() {
    cp ${WORKDIR}/${PV}/qoriq/H5*.c ${B}/src/
}

do_configure_append_intel-x86-common() {
    cp ${WORKDIR}/${PV}/cct/H5*.c ${B}/src/
}

do_install_append() {
    install -d ${D}/${libdir}/hdf5-tests
    cp -r ${B}/test/.libs/* ${D}/${libdir}/hdf5-tests/
    rm ${D}/${libdir}/hdf5-tests/libdynlib*
    rm ${D}/${libdir}/hdf5-tests/*.a
    rm ${D}/${libdir}/hdf5-tests/*.o
    rm ${D}/${libdir}/hdf5-tests/*.la
}
