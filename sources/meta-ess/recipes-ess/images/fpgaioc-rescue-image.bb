require recipes-core/images/core-image-minimal.bb

SUMMARY = "Image to be used for FPGA-IOC rescue OS"
DESCRIPTION = "FPGA-IOC rescue image"

IMAGE_INSTALL_append += " packagegroup-core-ssh-dropbear \
                        " 
IMAGE_FSTYPES="cpio.gz.u-boot"
IMAGE_ROOTFS_SIZE = "8192"
