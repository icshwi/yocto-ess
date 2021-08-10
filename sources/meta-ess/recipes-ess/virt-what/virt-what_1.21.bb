DESCRIPTION = "virt-what is a shell script which can be used to detect if the program is running in a virtual machine"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "https://people.redhat.com/~rjones/virt-what/files/virt-what-1.21.tar.gz"
SRC_URI[md5sum] = "d0359ff3d6278aa021df8cafbad89cd4"

inherit autotools
