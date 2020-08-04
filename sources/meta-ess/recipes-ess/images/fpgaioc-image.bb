require recipes-core/images/core-image-minimal.bb
require recipes-extended/images/core-image-full-cmdline.bb

SUMMARY = "Image to be used for FPGA-IOC"
DESCRIPTION = "FPGA-IOC image"

IMAGE_FEATURES += "package-management"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "i2c-slave-miniioc"

IMAGE_INSTALL_append += " packagegroup-base \
                          polkit \
                          eth-config \
                          nfs-utils \
                          readline \
                          boost \
                          curl \
                          libxml2 \
                          zlib \
                          libpng \
                          nano \
                          tree \
                          perl \
                          perl-module-posix \
                          sysfsutils \
                          sysklogd \
                          kmod \
                          kernel-modules \
                          sysstat \
                          pciutils \
                          screen \
                          procserv \
                          libtirpc \
                          dnf \
                          host-setup \
                          jpeg \
                          python3-pkgutil \
                          packagegroup-core-ssh-openssh \
                          kernel-module-i2c-slave-miniioc \
                          kernel-module-si5346 \
                          kernel-module-si5332 \
                          "

IMAGE_FSTYPES="cpio.gz.u-boot tar.gz ext3"
