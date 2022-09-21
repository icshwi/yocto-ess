FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Add patch to add LDB_MODULES_PATH to environment
SRC_URI += "file://0001-Add-LDB_MODULES_PATH-to-environment.patch"
SRC_URI += "file://0001-Add-ExecStartPre-to-create-log-directory.patch"

# Use systemd and autofs
PACKAGECONFIG += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
PACKAGECONFIG += " autofs"

PACKAGECONFIG[autofs] = "--with-autofs, --with-autofs=no"
PACKAGECONFIG[systemd] = "--with-initscript=systemd,--with-initscript=sysv"

inherit systemd

PR = "r1.3"

# Symlink /var/run -> /run conflicts with base-files package.
# So remove it
do_install_append () {
    rm -rf ${D}/var/run
    install -d ${D}/lib/security
    ln -s -r ${D}${libdir}/security/pam_sss.so ${D}/lib/security/pam_sss.so
}

SYSTEMD_SERVICE_${PN} = " \
    ${@bb.utils.contains('PACKAGECONFIG', 'autofs', 'sssd-autofs.service sssd-autofs.socket', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'curl', 'sssd-kcm.service sssd-kcm.socket', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'infopipe', 'sssd-ifp.service ', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'ssh', 'sssd-ssh.service sssd-ssh.socket', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'sudo', 'sssd-sudo.service sssd-sudo.socket', '', d)} \
    sssd-nss.service \
    sssd-nss.socket \
    sssd-pam-priv.socket \
    sssd-pam.service \
    sssd-pam.socket \
    sssd.service \
"

FILES_${PN} += "/lib/systemd /lib/security"
