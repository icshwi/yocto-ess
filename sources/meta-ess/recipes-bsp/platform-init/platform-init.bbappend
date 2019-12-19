FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append := " \
                file://ps7_init_gpl.c \
                file://ps7_init_gpl.h \
                "

do_install_append() {
	install -m 0644 ${WORKDIR}/ps7_init_gpl.c ${D}${PLATFORM_INIT_DIR}/
	install -m 0644 ${WORKDIR}/ps7_init_gpl.h ${D}${PLATFORM_INIT_DIR}/
}
