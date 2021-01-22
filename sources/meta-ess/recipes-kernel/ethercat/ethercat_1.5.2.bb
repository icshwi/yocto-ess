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

# Patch to update vm_fault struct type for kernel 5.4
SRC_URI += "file://0002-Update_vm_fault_definition_for_kernel_5_4.patch"

# Patch to replace old do_gettimeofday call with getnstimeofday (kernel 5.4)
SRC_URI += "file://0002-Replace-do-gettimeofday-with-getnstimeofday.patch"

# Patch to fix path to uapi/linux/sched/types.h (kernel 5.4)
SRC_URI += "file://0003-Fix-include-path-for-uapi-linux-sched-types-header.patch"

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
EXTRA_OECONF += "--enable-cycles=yes --enable-hrtimer=no"
EXTRA_OECONF += "-enable-regalias=no --enable-tool=yes"
EXTRA_OECONF += "-enable-userlib=yes --enable-sii-assign=yes" 
EXTRA_OECONF += "-enable-rt-syslog=yes --prefix=/opt/etherlab"
EXTRA_OECONF += "--with-linux-dir=${STAGING_KERNEL_BUILDDIR}"
EXTRA_OECONF += "${STATICLIBCONF}"

RPROVIDES_${PN} += "kernel-module-ethercat-master-${PV}"
PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
PROVIDES = "${PN} ${PN}-dbg ${PN}-dev"

TARGET_CFLAGS += "-I ${STAGING_INCDIR}"
TARGET_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"

MAKE_TARGETS = "all modules"

# Use autotools-brokensep instead of autotools, as 
# the EtherLAB master has issues building in a 
# separate build directory, i.e. outside the source
inherit autotools-brokensep module systemd

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
    install -d ${D}${sbindir}
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}/etc/udev/rules.d
    install -d ${D}${systemd_system_unitdir}
    
    # Copy ethercat tool binary to bin dir
    install -m 0755 ${S}/tool/ethercat ${D}${bindir}/ethercat
    
    # Remove unused example modules
    rm -rf ${D}/lib/modules/${KERNEL_VERSION}/ethercat/examples

    # Install udev rule
    install -m 0666 ${WORKDIR}/99-ethercat.rules ${D}/etc/udev/rules.d/99-ethercat.rules

    # Install systemd service and conf file
    install -m 0644 ${S}/script/ethercat.conf ${D}/etc
    install -m 0755 ${S}/script/ethercatctl ${D}${sbindir}/ethercatctl
    install -m 0644 ${WORKDIR}/ethercat.service ${D}${systemd_system_unitdir}  
}

FILES_${PN}-dev += "/usr/include/ethercat/Module.symvers"
FILES_${PN}     += "/usr/bin /usr/sbin /etc/udev/rules.d /lib/systemd /etc/ethercat.conf"

SYSTEMD_SERVICE_${PN} = "ethercat.service"
PACKAGE_ARCH = "${MACHINE_ARCH}"
