SUMMARY = "power manager miniioc module"
LICENSE = "CLOSED"

inherit module

SRC_URI = "file://Makefile \
           file://pm-miniioc.c \
          "

S = "${WORKDIR}"

RPROVIDES_${PN} += "kernel-module-pm-miniioc"

KERNEL_MODULE_AUTOLOAD += "pm-miniioc "
