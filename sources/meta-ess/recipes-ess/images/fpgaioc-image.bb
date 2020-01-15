require recipes-core/images/core-image-minimal.bb
require recipes-extended/images/core-image-full-cmdline.bb

SUMMARY = "Image to be used for FPGA-IOC"
DESCRIPTION = "FPGA-IOC image"

IMAGE_FEATURES += "package-management"
IMAGE_INSTALL_append = " \
    packagegroup-core-ssh-openssh \
    openssh-sftp-server \ 
    packagegroup-base \
        e2fsprogs-e2fsck e2fsprogs-mke2fs e2fsprogs-tune2fs e2fsprogs-badblocks e2fsprogs-resize2fs \
        dosfstools \
        i2c-tools \
        kernel-module-i2c-slave-miniioc \
        "

IMAGE_FSTYPES="cpio.gz tar.gz ext3"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "device-tree \
			     	     i2c-slave-miniioc \
			     	     "
