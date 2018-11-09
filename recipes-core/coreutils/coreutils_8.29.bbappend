# [ df mktemp base64 link gets a special treatment and is not included in this
bindir_progs = "arch basename chcon cksum comm csplit cut dir dircolors dirname du \
                env expand expr factor fmt fold groups head hostid id install \
                join logname md5sum mkfifo nice nl nohup nproc od paste pathchk \
                pinky pr printenv printf ptx readlink realpath runcon seq sha1sum sha224sum sha256sum \
                sha384sum sha512sum shred shuf sort split stdbuf sum tac tail tee test timeout\
                tr truncate tsort tty unexpand uniq unlink uptime users vdir wc who whoami yes"

do_install_append() {
	for i in link; do mv ${D}${bindir}/$i ${D}${bindir}/$i.${BPN}; done
}

ALTERNATIVE_${PN} = "lbracket ${bindir_progs} ${base_bindir_progs} ${sbindir_progs} base64 mktemp df link"
ALTERNATIVE_${PN}-doc = "base64.1 mktemp.1 df.1 link.1 groups.1 kill.1 uptime.1 stat.1  hostname.1"

ALTERNATIVE_LINK_NAME[link] = "${base_bindir}/link"
ALTERNATIVE_TARGET[link] = "${bindir}/link.${BPN}"
ALTERNATIVE_LINK_NAME[link.1] = "${mandir}/man1/link.1"

