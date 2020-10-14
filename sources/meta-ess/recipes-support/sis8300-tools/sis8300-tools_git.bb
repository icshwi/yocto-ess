DESCRIPTION = "Helper utilities for use with the Struck SIS8300 Digitizer AMC"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=73f1eb20517c55bf9493b7dd6e480788"

#RDEPENDS_${PN} = "sis8300-dev"
INSANE_SKIP_${PN} = "ldflags"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""


SRC_URI = "git://git@gitlab.esss.lu.se/epics-modules/sis8300drv.git;branch=master;protocol=ssh \
           file://0001-Update-Makefiles-for-cross-compilation.patch \
"

PV = "0.0.1"
SRCREV = "327c9420bdad485635fed1a462b4f60715baeb0c"

S = "${WORKDIR}/git"

#do_configure() {
#}

do_compile_prepend() {
    cd src/main/c/lib
    make
    cd ../tools
}

do_install() {
   install -d ${D}${libdir}
   install -m 0755 ${S}/src/main/c/lib/libsis8300drv.so ${D}${libdir}

   install -d ${D}/usr/bin
   cp ${S}/src/main/c/tools/acquisition/sis8300drv_acq   ${D}/usr/bin/sis8300drv_acq
   cp ${S}/src/main/c/tools/flash/sis8300drv_flashfw     ${D}/usr/bin/sis8300drv_flashfw
   cp ${S}/src/main/c/tools/fwver/sis8300drv_fwver       ${D}/usr/bin/sis8300drv_fwver
   cp ${S}/src/main/c/tools/i2c_rtm/sis8300drv_i2c_rtm   ${D}/usr/bin/sis8300drv_i2c_rtm
   cp ${S}/src/main/c/tools/i2c_temp/sis8300drv_i2c_temp ${D}/usr/bin/sis8300drv_i2c_temp
   cp ${S}/src/main/c/tools/irq/sis8300drv_irq           ${D}/usr/bin/sis8300drv_irq
   cp ${S}/src/main/c/tools/memory/sis8300drv_mem        ${D}/usr/bin/sis8300drv_mem
   cp ${S}/src/main/c/tools/mmap/sis8300drv_mmap         ${D}/usr/bin/sis8300drv_mmap
   cp ${S}/src/main/c/tools/output/sis8300drv_out        ${D}/usr/bin/sis8300drv_out
   cp ${S}/src/main/c/tools/performance/sis8300drv_perf  ${D}/usr/bin/sis8300drv_perf
   cp ${S}/src/main/c/tools/register/sis8300drv_reg      ${D}/usr/bin/sis8300drv_reg
   cp ${S}/src/main/c/tools/speed/sis8300drv_speed       ${D}/usr/bin/sis8300drv_speed
}
