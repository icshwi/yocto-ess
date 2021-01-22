require recipes-core/images/core-image-minimal.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

CORE_IMAGE_EXTRA_INSTALL += "udev-extraconf"

IMAGE_FSTYPES = "tar.gz cpio.gz"

SUMMARY = "Image to be used for IFC1410 and IFC1420"
DESCRIPTION = "IFC14XX image"

LICENSE = "MIT"

IMAGE_FEATURES += "package-management"
IMAGE_INSTALL_append = " \
    packagegroup-core-ssh-openssh \
"

