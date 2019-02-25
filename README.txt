===================================
Setting up to use the Yocto project
===================================
$: git clone --recurse-submodules git@gitlab.esss.lu.se:ioxos/yocto-ioxos.git

===============
Building images
===============
$: . ./setup-env -m ifc14xx-64b -t 4 -j4 (ifc14xx-rt-64b for rt kernel, -t <number of parallel tasks>, -j <number of parallel jobs>)
$: bitbake ifc14xx-image

Images will be found under tmp/deploy/images/ifc14xx-64b/

=======================
File bug/task
=======================
Issue tracker: https://jira.esss.lu.se
Project: ICS HW and Integration
Issue type: Task or Bug
Component: ICS_HWI_WP04
