PR = "r1.0"

# Add symlink for /lib/security/pam_pwquality.so -> /usr/lib/security/pam_pwquality.so
do_install_append () {
    install -d ${D}/lib/security
    ln -s -r ${D}${libdir}/security/pam_pwquality.so ${D}/lib/security/pam_pwquality.so
}

# The package contains symlinks that trip up insane
INSANE_SKIP_${PN} = "dev-so"

FILES_${PN} += "/lib/security"
