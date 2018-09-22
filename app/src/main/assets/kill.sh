#!/bin/bash

# WHITE_LIST_FILE   # exported in java code.
# KILL_DIR          # exported in java code.
PS_LIST_FILE="${KILL_DIR}/ps.txt"
PKG_LIST_FILE="${KILL_DIR}/pkg.txt"

if [ ! -d "${KILL_DIR}" ]; then
    mkdirs "${KILL_DIR}"
fi

function filter_pkg_name() {
    ps -A | grep "^u0_" > ${PS_LIST_FILE}
    local item
    local pkg
    while read line; do
        item=(${line})
        pkg=${item[8]}
        pkg=${pkg%:*}
        if [[ ${pkg} == \[*\] ]]; then    # 忽略 [] 标识进程
            continue
        elif [[ ${pkg} == *.so ]]; then   # 忽略 so 进程
            continue
        elif [[ ${pkg} == su ]]; then     # 忽略 su 进程
            continue
        elif [[ ${pkg} == com.google.* || ${pkg} == com.android.* ]]; then
            continue
        fi
        echo ${pkg}
    done < ${PS_LIST_FILE}
}

function main() {
    filter_pkg_name | sort | uniq > ${PKG_LIST_FILE}
    while read line; do
        if [ -n "$(grep ${line} ${WHITE_LIST_FILE})" ]; then
            continue
        fi
        am force-stop ${line}
        echo ${line}
    done < ${PKG_LIST_FILE}
}

main "$@"
