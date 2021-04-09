LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

BBCLASSEXTEND = "native"

# Mercurial repository for EtherLAB master on SourceForge
SRC_URI = "hg://hg.code.sf.net/p/etherlabmaster;protocol=http;module=code"

# The EtherLAB master requires an unoffial patchset maintained by
# Gavin Lambert <gavin.lambert@tomra.com>, available from
# http://hg.code.sf.net/u/uecasm/etherlab-patches
# For ease of application, this patchset has been condensed into
# a single bitbake-compatible patch file...
SRC_URI += "file://0001-etherlab-patchset.patch"

# Src revision of code from Mercurial repository
SRCREV="33b922ec1871"
# Build number - increment on change
PR = "r0"

# Requires the EtherLAB EtherCAT master driver to work
RDEPENDS_${PN} = "ethercat"

STATICLIBCONF = "--disable-static"

# Extra flags to provide to ./configure
EXTRA_OECONF =  "--enable-generic=no --enable-8139too=no"
EXTRA_OECONF += "--enable-hrtimer=no --enable-shared=no"
EXTRA_OECONF += "--enable-regalias=no --enable-tool=yes"
EXTRA_OECONF += "--enable-userlib=yes --enable-sii-assign=yes" 
EXTRA_OECONF += "--enable-rt-syslog=yes --prefix=/opt/etherlab"
EXTRA_OECONF += "--with-linux-dir=${STAGING_KERNEL_BUILDDIR}"

EXTRA_OECONF_append_intel = " --enable-cycles=yes"
EXTRA_OECONF_append_zynq  = " --enable-cycles=no --disable-e1000"
EXTRA_OECONF_append_zynq += " --disable-e1000e --disable-e100"

TARGET_CFLAGS += "-I ${STAGING_INCDIR}"
TARGET_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"

MAKE_TARGETS = "all modules"

# Use autotools-brokensep instead of autotools, as 
# the EtherLAB master has issues building in a 
# separate build directory, i.e. outside the source
inherit autotools-brokensep

# Kernel staging directory must be populated for configure to
# run successfully
do_configure[depends] += "virtual/kernel:do_shared_workdir"

# Set source directory
S = "${WORKDIR}/code"

# Create ChangeLog in source dir
# (requirement of autoreconf call)
do_configure_prepend() {
    cd ${S}
    touch ChangeLog
    cd ${B}
}

do_install_append() {
    # Create directories
    install -d ${D}${bindir}
    
    # Copy ethercat tool binary to bin dir
    # install -m 0755 ${S}/tool/ethercat ${D}${bindir}/ethercat

    # Remove unwanted service files
    rm -rf ${D}/usr/lib/systemd
    rm -rf ${D}/usr/sbin
    rm -rf ${D}/usr/lib
    rm -rf ${D}/etc
}
