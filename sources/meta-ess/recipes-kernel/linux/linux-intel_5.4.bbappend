FILESEXTRAPATHS_prepend := "${THISDIR}/linux-intel:"

SRC_URI_append = " file://Enable-PCIE-hotplug-driver.cfg \
                   file://Enable-DMA-CMA.cfg \
                   file://Disable-hyper-dmabuf.cfg \
                   "
