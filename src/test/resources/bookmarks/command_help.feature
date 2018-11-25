Feature: User can get help

	Scenario: User can type help to get command list
		When command help is selected
		Then system will respond with the main help page

#	Scenario: Command list is printed at startup
#		Then system will respond with the main help page
