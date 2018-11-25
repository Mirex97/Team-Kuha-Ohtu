Feature: User can add a new bookmark

	Scenario: User can add a new bookmark
		When command add is selected
		And type "book" is given
		And title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-1", description "Good fantasy book", comment "" and tags "fantasy, steampunk" are given
		Then system will respond with "Entry created"
		And entry ID 1 has title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-1", description "Good fantasy book", comment "" and tags "fantasy, steampunk"
