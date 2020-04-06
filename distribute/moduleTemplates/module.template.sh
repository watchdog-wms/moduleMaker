#!/bin/bash
SCRIPT_FOLDER=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
source $SCRIPT_FOLDER/../../core_lib/includeBasics.sh

# check, if used tools are installed
# TODO: add tools which are used in this script here in order to check, if they are installed on the system
USED_TOOLS={%USED_TOOLS%}
MESSAGE=$($LIB_SCRIPT_FOLDER/checkUsedTools.sh "$USED_TOOLS" "check_shflag_tools")
CODE=$?

if [ $CODE -ne 0 ]; then
	echoError "$MESSAGE"
	exit $EXIT_TOOLS_MISSING
fi

# define parameters
{%VARIABLES%}
DEFINE_boolean 'version' 'false' '[optional] prints the version' 'v'
DEFINE_boolean 'debug' 'false' '[optional] prints out debug messages.' ''

# parse parameters
FLAGS "$@" || exit $EXIT_INVALID_ARGUMENTS
eval set -- "${FLAGS_ARGV}"
printParamValues "initial parameters" # print param values, if in debug mode

if [ "$FLAGS_version" -eq 0 ]; then
	# TODO obtain version of important external software or remove --version flag
	MESSAGE=$(TODO)
	echo $MESSAGE
	exit $EXIT_OK
fi

# check if mandatory arguments are there
{%MANDATORY_VAR_CHECK%}

# further parameter checks  which refer to input ranges or valid parameters in general
{%FURTHER_PARAMETER_CHECKS%}

# check, if the input files exist
{%FILE_EXISTENCE_CHECK%}


printParamValues "parameters before actual script starts" # print param values, if in debug mode
##################################################### START with actual SCRIPT ##################################################### 

# <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< #
# functional part of the script
{%FUNCTIONAL_PART%}


RET=$?

# check for error
FAIL=0
TMP_FILE=$(getTmpFile {%MODULE_NAME%})
touch "$TMP_FILE"
trap "rm -f \"$TMP_FILE\" 2>&1 > /dev/null" EXIT
printf "$MESSAGE" > "$TMP_FILE"
MESSAGE_ERROR=$($SCRIPT_FOLDER/../../core_lib/errorChecker.sh "$TMP_FILE" "truncated file:fail to open file" 2>&1)
CODE_ERROR=$?
rm -f "$TMP_FILE" 2>&1 > /dev/null

if [ $CODE_ERROR -ne 0 ]; then
	echoError "Error checker found some errors, see found errors below"
	echo -e "$MESSAGE_ERROR"
	exit $EXIT_FAILED
else
	# check exit code
	if [ $FAIL -eq 0 ] && [ $RET -eq 0 ]; then
		# output the original message
		printf "$MESSAGE\n"
		{%RETURN_PARAMETER%}
		exit $EXIT_OK
	else
		FAIL=1
	fi
	if [ $FAIL -eq 1 ]; then
		echoError "Run failed. See error below"
		echoAError "error code: '$RET'"
		 # output the original message
		printf "$MESSAGE\n"
		exit $EXIT_FAILED
	fi
fi


# <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< #

# exit with exit status 0 or exit code should be there earlier
echoError "Reached end of script! Exit should be performed earlier..."
exit $EXIT_REACHED_END_OF_SCRIPT
