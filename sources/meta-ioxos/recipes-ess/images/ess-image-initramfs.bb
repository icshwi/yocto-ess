include ess-image.bb

PACKAGE_ARCH = "${MACHINE_ARCH}"

CORE_IMAGE_EXTRA_INSTALL += "udev-extraconf lsb"
CORE_IMAGE_EXTRA_INSTALL_append_qoriq = " udev-rules-qoriq"

DESCRIPTION = "ESS initramfs image"
LICENSE = "MIT"

IMAGE_FSTYPES += " cpio.gz"
PACKAGE_INSTALL = "${IMAGE_INSTALL}"

