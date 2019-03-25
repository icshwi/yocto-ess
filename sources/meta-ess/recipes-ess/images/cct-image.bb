require recipes-core/images/core-image-minimal.bb

SUMMARY = "Image to be used for Concurrent CPU cards0"
DESCRIPTION = "CCT image"

IMAGE_FEATURES += "package-management"
IMAGE_INSTALL_append = " \
    packagegroup-core-ssh-openssh \
"

IMAGE_FSTYPES += " cpio.gz"

