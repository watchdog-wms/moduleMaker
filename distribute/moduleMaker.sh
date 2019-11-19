#!/bin/bash
SCRIPT_FOLDER=$(cd $(dirname $(readlink -f "${BASH_SOURCE[0]}" 2>/dev/null || readlink "${BASH_SOURCE[0]}" || echo "${BASH_SOURCE[0]}")) && pwd -P)
cd "${SCRIPT_FOLDER}/../moduleMaker/"
source "${SCRIPT_FOLDER}/../core_lib/javafxLauncher.sh" "../moduleMaker/moduleMaker.jar" $@
