Feature: User can add a new bookmark

	Scenario: User can add a new bookmark
		When command add is selected
		And type "book" is given
		And title "The Three-Body Problem", author "Cixin Liu", isbn "978-7-5366-9293-0", description "Good science fiction book", comment "" and tags "scifi" are given
		Then system will respond with "Entry created"
