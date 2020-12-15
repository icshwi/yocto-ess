# Symlink /var/run -> /run conflicts with base-files package.
# So remove it
do_install_append () {
    rm -rf ${D}/var/run
}
