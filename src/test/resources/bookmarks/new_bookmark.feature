Feature: User can add a new bookmark

	Scenario: User can add a new bookmark
		When command add is selected
		And type "book" is given
		And title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-1", description "Good fantasy fiction book", comment "" and tags "fantasy, steampunk" are given
		Then system will respond with "Entry created"
		Then entry ID 1 has title "The Three-Body Problem", author "Cixin Liu", isbn "978-7-5366-9293-0", description "Good science fiction book", comment "" and tags "scifi"
