Feature: User can add a new bookmark

	Scenario: User can add a new bookmark
		When command "add" is selected
		And type "book" is given
		And title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-7", description "Good fantasy book", comment "" and tags "fantasy, steampunk" are given
		Then system will respond with "Entry created"
		And book entry ID 1 has title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-7", description "Good fantasy book", comment "" and tags "fantasy, steampunk"

	Scenario: Creating a bookmark creates tags if they don't exist
		Given the book "The Empire Strikes Back" by "Donald F. Glut" with ISBN "978-0-345-28392-4", description "none" and tags "scifi" has been added
		And the book "Northern Lights" by "Philip Pullman" with ISBN "978-0-590-54178-7", description "non" and tags "scifi, fantasy" has been added
		And user has entered tag section
		When tag command "list" is selected
		Then system will respond with "tag #1: scifi"
		And system will respond with "tag #2: fantasy"

	Scenario: Creating a bookmark doesn't create tags if they do exist
		Given the book "The Empire Strikes Back" by "Donald F. Glut" with ISBN "978-0-345-28392-4", description "none" and tags "scifi" has been added
		And the book "Northern Lights" by "Philip Pullman" with ISBN "978-0-590-54178-7", description "non" and tags "scifi" has been added
		And user has entered tag section
		When tag command "list" is selected
		Then system will respond with "tag #1: scifi"

	Scenario: Type is asked again if user provides invalid type
		When command "add" is selected
		And type "hmm" is given
		Then system will respond with "Unrecognized type. Choose one of: meme book podcast video blog article"
		When type "book" is given
		And title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-7", description "Good fantasy book", comment "" and tags "fantasy, steampunk" are given
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
