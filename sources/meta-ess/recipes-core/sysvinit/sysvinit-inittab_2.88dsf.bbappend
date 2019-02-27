pkg_postinst_${PN} () {
# run this on the target
if [ "${SERIAL_CONSOLES_CHECK}" = "" ]; then
	exit 0
fi
}

pkg_postinst_ontarget_${PN} () {
# run this on the target
if [ "x$D" = "x" ] && [ -e /proc/consoles ]; then
	tmp="${SERIAL_CONSOLES_CHECK}"
	for i in $tmp
	do
		j=`echo ${i} | sed -e s/^.*\;//g -e s/\:.*//g`
		k=`echo ${i} | sed s/^.*\://g`
		if [ -z "`grep ${j} /proc/consoles`" ]; then
			if [ -z "${k}" ] || [ -z "`grep ${k} /proc/consoles`" ] || [ ! -e /dev/${j} ]; then
				sed -i -e /^.*${j}\ /d -e /^.*${j}$/d /etc/inittab
			fi
		fi
	done
	kill -HUP 1
else
	exit 1
fi
}

