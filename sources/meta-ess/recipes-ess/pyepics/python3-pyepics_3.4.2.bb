SUMMARY = "Epics Channel Access for Python"
HOMEPAGE = "http://pyepics.github.io/pyepics/"
AUTHOR = "Matthew Newville <newville@cars.uchicago.edu>"
LICENSE = "epics-common"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f113138f4249db9ba9176b8188ac2520"

SRC_URI = "https://files.pythonhosted.org/packages/d7/9e/c3e74b3f9b81d2a9beab467dad1fd156e0ab978a49cf787f36f398caeef4/pyepics-3.4.2.tar.gz"

# Replacement shared libraries for ESS Linux - the included libraries were for CentOS 7...
#
# Libraries taken from E3/EPICS base-7.0.3.1 compiled for linux-corei7-poky
#
#     ${EPICS_BASE}/lib/linux-corei7-poky/libca.so.4.13.5
#     ${EPICS_BASE}/lib/linux-corei7-poky/libCom.so.3.17.6
#
# Libraries taken from E3/EPICS base-7.0.3.1 compiled for linux-ppc64e6500
#
#     ${EPICS_BASE}/lib/linux-ppc64e6500/libca.so.4.13.5
#     ${EPICS_BASE}/lib/linux-ppc64e6500/libCom.so.3.17.6
#
# And configured using patchelf to change modify libCom -> libComPYEPICS
# And update the libca dependency on libCom
#
# ( Procedure from https://github.com/pyepics/pyepics/blob/master/epics/clibs/linux64/README )
#
#     mv libCom.so libComPYEPICS.so
#     patchelf --set-soname libca.so libca.so
#     patchelf --set-soname libComPYEPICS.so libComPYEPICS.so
#     patchelf --set-rpath '$ORIGIN' libca.so
#     patchelf --set-rpath '$ORIGIN' libComPYEPICS.so
#     patchelf --replace-needed libCom.so libComPYEPICS.so libca.so
#

SRC_URI += "${EPICS_LIB_FILES}"

EPICS_LIB_FILES ?= " \
"

EPICS_LIB_FILES_intel-x86-common = " \
    file://cct/libca.so \
    file://cct/libComPYEPICS.so \
"

EPICS_LIB_FILES_qoriq = " \
    file://qoriq/libca.so \
    file://qoriq/libComPYEPICS.so \
"

# Patch to remove unused architectures (win32, darwin, arm, linux32)
SRC_URI += "file://0001-Remove-unused-architectures.patch"

SRC_URI[md5sum] = "398d808f987de79c0547392fc007ed21"
SRC_URI[sha256sum] = "673fc8f6c8a2663c15473938fd3b55c2d3431dc739aa479b6d9d005373219068"

S = "${WORKDIR}/pyepics-3.4.2"
# Increment revision number if package changes...
PR = "r1"

inherit setuptools3 pypi
require python3-versions.inc

RDEPENDS_${PN} = "python3-setuptools"

distutils3_do_configure_append_intel-x86-common() {
    cp ${WORKDIR}/cct/*.so ${S}/epics/clibs/linux64/
}

distutils3_do_configure_append_qoriq() {
    cp ${WORKDIR}/qoriq/*.so ${S}/epics/clibs/linux64/
}
BBCLASSEXTEND = "native nativesdk"
