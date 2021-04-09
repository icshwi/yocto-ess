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

# Patch to modify the conf/systemd files for CCT
SRC_URI += "file://0002-modify-ethercat-config-for-cct.patch"

# udev rule to enable automatic device node creation
SRC_URI += "file://99-ethercat.rules"

# Systemd serveice
SRC_URI += "file://ethercat.service"

# Src revision of code from Mercurial repository
SRCREV="33b922ec1871"
# Build number - increment on change
PR = "r0"

STATICLIBCONF = "--disable-static"

# Extra flags to provide to ./configure
EXTRA_OECONF =  "--enable-generic=yes --enable-8139too=no"
EXTRA_OECONF += "--enable-hrtimer=no --enable-shared=yes"
EXTRA_OECONF += "--enable-regalias=no --enable-tool=yes"
EXTRA_OECONF += "--enable-userlib=yes --enable-sii-assign=yes"
EXTRA_OECONF += "--enable-rt-syslog=yes --prefix=/opt/etherlab"
EXTRA_OECONF += "--with-linux-dir=${STAGING_KERNEL_BUILDDIR}"

EXTRA_OECONF_append_intel = " --enable-cycles=yes"
EXTRA_OECONF_append_zynq  = " --enable-cycles=no --disable-e1000"
EXTRA_OECONF_append_zynq += " --disable-e1000e --disable-e100"

RPROVIDES_${PN} += "kernel-module-ethercat-master-${PV}"
PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
PROVIDES = "${PN} ${PN}-dbg ${PN}-dev"

TARGET_CFLAGS += "-I ${STAGING_INCDIR}"

MAKE_TARGETS = "modules"

# Use autotools-brokensep instead of autotools, as
# the EtherLAB master has issues building in a
# separate build directory, i.e. outside the source
inherit autotools-brokensep module systemd

# Kernel staging directory must be populated for the 
# compile stage to run successfully
do_compile[depends] += "virtual/kernel:do_shared_workdir"

# Set source directory
S = "${WORKDIR}/code"

# Create ChangeLog in source dir
# (requirement of autoreconf call)
do_configure_prepend() {
    cd ${S}
    touch ChangeLog
    cd ${B}
}

module_do_compile_append() {
    cd ${S}/lib
    oe_runmake
}

do_install_append() {
    # Create directories
    install -d ${D}${sbindir}
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}/etc/udev/rules.d
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${libdir}/etherlab
    install -d ${D}/usr/include/etherlab  
 
    # Install libraries
    install -m 0755 ${S}/lib/.libs/libethercat.so ${D}${libdir}/etherlab
    install -m 0755 ${S}/lib/.libs/libethercat.so.1 ${D}${libdir}/etherlab
    install -m 0755 ${S}/lib/.libs/libethercat.so.1.1.0 ${D}${libdir}/etherlab
    install -m 0755 ${S}/lib/libethercat.la ${D}${libdir}/etherlab

    # Install headers
    install -m 0644 ${S}/include/*.h ${D}/usr/include/etherlab

    # Remove unused example modules
    rm -rf ${D}/lib/modules/${KERNEL_VERSION}/ethercat/examples

    # Install udev rule
    install -m 0666 ${WORKDIR}/99-ethercat.rules ${D}/etc/udev/rules.d/99-ethercat.rules

    # Install systemd service and conf file
    install -m 0644 ${S}/script/ethercat.conf ${D}/etc
    install -m 0755 ${S}/script/ethercatctl ${D}${sbindir}/ethercatctl
    install -m 0644 ${WORKDIR}/ethercat.service ${D}${systemd_system_unitdir}
}

FILES_${PN}-dev += "/usr/include/ethercat/Module.symvers /usr/lib/etherlab/libethercat.so.1 /usr/lib/etherlab/libethercat.so.1.1.0"
FILES_${PN}     += "/usr/sbin /etc/udev/rules.d /lib/systemd /etc/ethercat.conf /usr/lib/etherlab/libethercat.so"

SYSTEMD_SERVICE_${PN} = "ethercat.service"
PACKAGE_ARCH = "${MACHINE_ARCH}"
