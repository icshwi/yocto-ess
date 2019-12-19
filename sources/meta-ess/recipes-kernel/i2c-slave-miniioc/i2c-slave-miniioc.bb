SUMMARY = "i2c slave miniioc module"
LICENSE = "CLOSED"

inherit module

SRC_URI = "file://Makefile \
           file://i2c-slave-miniioc.c \
          "

S = "${WORKDIR}"

RPROVIDES_${PN} += "kernel-module-i2c-slave-miniioc"

KERNEL_MODULE_AUTOLOAD += "i2c-slave-miniioc "
