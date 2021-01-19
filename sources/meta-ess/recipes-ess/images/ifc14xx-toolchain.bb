require recipes-core/meta/meta-toolchain.bb

MULTILIBS_pn-${PN} = ""
TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
TOOLCHAIN_TARGET_TASK += " \
    dtc-staticdev \
    glib-2.0 \
    glib-2.0-dev \
    libgomp \
    libgomp-dev \
    libgomp-staticdev \
    libstdc++-staticdev \
    ${TCLIBC}-staticdev \
    systemd-dev \
    libxml2-dev \
    libtirpc \
    jpeg \
    boost \
    curl \
    hdf5 \
    tiff \ 
    netcdf-c-dev \
    c-blosc-staticdev \
    gawk \
    kernel-devsrc \
"


TOOLCHAIN_HOST_TASK += " \
    nativesdk-dtc \
    nativesdk-u-boot-mkimage \
    nativesdk-cst \
    nativesdk-perl-modules \
"
