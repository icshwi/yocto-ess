require recipes-core/meta/meta-toolchain.bb

TOOLCHAIN_TARGET_TASK += " \
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
    kernel-devsrc \
    ethercat-dev \
"

TOOLCHAIN_HOST_TASK += " \
    nativesdk-perl-modules \
"
