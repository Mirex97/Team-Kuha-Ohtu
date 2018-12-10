Feature: User can get help

	Scenario: User can type help to get command list
		When command "help" is selected
		Then system will respond with the main help page

	Scenario: User can type help to get command list and exit and get back
		When command "help" is selected
                And system will respond with the main help page without exit
                And user types "back"
                And command "help" is selected
                Then system will respond with the main help page

	Scenario: User can type help in tags to get command list
		Given user has entered tag section
		When tag command "help" is selected
		Then system will respond with the tag help page
