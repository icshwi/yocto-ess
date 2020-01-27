# Disable KILL_USER_PROCESSES
do_install_append() {
    sed -i -e 's/#KillUserProcesses=yes/KillUserProcesses=no/' ${D}${sysconfdir}/systemd/logind.conf
}
