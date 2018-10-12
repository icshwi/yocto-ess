require recipes-core/images/core-image-minimal.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

CORE_IMAGE_EXTRA_INSTALL += "udev-extraconf lsb"
CORE_IMAGE_EXTRA_INSTALL_append_qoriq = " udev-rules-qoriq"

IMAGE_FSTYPES = "tar.gz"

SUMMARY = "Image to be used for IFC1410 and IFC1420"
DESCRIPTION = "ESS image"

LICENSE = "MIT"

IMAGE_INSTALL_append = " \
    packagegroup-core-ssh-openssh \
    packagegroup-fsl-mfgtools \
    packagegroup-fsl-tools-core \
    packagegroup-fsl-networking-core \
"

