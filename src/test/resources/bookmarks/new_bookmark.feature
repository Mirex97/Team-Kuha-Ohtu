Feature: User can add a new bookmark

	Scenario: User can add a new bookmark
		When command "add" is selected
		And type "book" is given
		And title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-1", description "Good fantasy book", comment "" and tags "fantasy, steampunk" are given
		Then system will respond with "Entry created"
		And book entry ID 1 has title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-1", description "Good fantasy book", comment "" and tags "fantasy, steampunk"

	Scenario: Type is asked again if user provides invalid type
		When command "add" is selected
		And type "hmm" is given
		Then system will respond with "Unrecognized type. Choose one of: meme book podcast video blog article"
		When type "book" is given
		And title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-1", description "Good fantasy book", comment "" and tags "fantasy, steampunk" are given
		Then system will respond with "Entry created"

	Scenario: Adding can be cancelled before giving type
		When command "add" is selected
		Then system will respond with "Type: "
		When CTRL+D is pressed
		Then system will respond with "Adding cancelled"

	Scenario: Adding can be cancelled when entering metadata
		When command "add" is selected
		And type "book" is given
		Then system will respond with "Title: "
		When CTRL+D is pressed
		Then system will respond with "Adding cancelled"
