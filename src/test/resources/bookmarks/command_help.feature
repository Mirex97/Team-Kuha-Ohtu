Feature: User can get help

	Scenario: User can type help to get command list
		When command help is selected
		Then system will respond with "add    - add a new entry"

	Scenario: Command list is printed at startup
		Then system will respond with "add    - add a new entry"
