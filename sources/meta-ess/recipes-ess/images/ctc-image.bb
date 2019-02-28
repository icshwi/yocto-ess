require recipes-core/images/core-image-minimal.bb

SUMMARY = "Image to be used for Concurrent CPU cards0"
DESCRIPTION = "CTC image"

IMAGE_INSTALL_append = " \
    packagegroup-core-ssh-openssh \
"

