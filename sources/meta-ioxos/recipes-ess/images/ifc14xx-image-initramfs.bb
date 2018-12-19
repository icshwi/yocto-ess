include ifc14xx-image.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

CORE_IMAGE_EXTRA_INSTALL += "udev-extraconf lsb"

DESCRIPTION = "IFC14XX initramfs image"
LICENSE = "MIT"

IMAGE_FSTYPES += " cpio.gz"
PACKAGE_INSTALL = "${IMAGE_INSTALL}"

