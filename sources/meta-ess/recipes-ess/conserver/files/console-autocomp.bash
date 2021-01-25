# Nicklas Holmberg, European Spallation Source, 2019

_console_ioc_completions()
{
	# Stop autocomplete if one argument already is supplied
	if [ "${#COMP_WORDS[@]}" != "2" ]; then
    		return
	fi

	# Search through the output from 'console -u' and
	# filter by using the awk and only print the first
	# word found
	COMPREPLY=($(compgen -W "$(console -u | awk '{print $1;}')" -- "${COMP_WORDS[1]}"))
}

# autocomplete available for 'console' command
complete -F _console_ioc_completions console
