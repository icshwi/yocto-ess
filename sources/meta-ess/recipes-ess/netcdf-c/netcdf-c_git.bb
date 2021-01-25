DESCRIPTION = "Unidata network Common Data Form"
LICENSE = "unidata"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=17c864f08a4445fe9c41226785e9ed10"

SRC_URI = "git://github.com/Unidata/netcdf-c.git;nobranch=1;protocol=https \
"
SRCREV = "09bb524f7b8e0786280222555740be244ad853ce"

S = "${WORKDIR}/git"
# Increment revision number if package changes...
PR = "r1"

EXTRA_OECMAKE += "-DCMAKE_PREFIX_PATH=${D}${exec_prefix} -DENABLE_NETCDF_4=OFF -DENABLE_DAP=OFF -DENABLE_TESTS=OFF -DBUILD_SHARED_LIBS=ON"
inherit cmake

PACKAGES = "${PN} ${PN}-dev ${PN}-doc ${PN}-dbg"
PROVIDES = "${PN} ${PN}-dev ${PN}-doc ${PN}-dbg"

FILES_${PN} += "/usr/bin/[a-z]* /usr/lib64/libnetcdf.settings /usr/lib/libnetcdf.settings"
FILES_${PN}-doc += "/usr/share"
FILES_${PN}-dev += "/usr/bin/[a-z]* /usr/include /usr/lib64 /usr/lib"
FILES_${PN}-dbg += "/usr/bin/.debug /usr/src/debug"

