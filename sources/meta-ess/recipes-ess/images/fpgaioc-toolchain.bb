require recipes-core/meta/meta-toolchain.bb

TOOLCHAIN_TARGET_TASK += " \
    systemd-dev \
    libxml2-dev \
    libtirpc \
    jpeg \
    boost \
    curl \
    tiff \
    netcdf-c-dev \
    c-blosc-staticdev \
    kernel-devsrc \
"
# TODO: 
# Re-enable removed packages:
# * hdf5
