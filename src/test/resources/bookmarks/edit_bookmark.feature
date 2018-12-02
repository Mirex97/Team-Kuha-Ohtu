Feature: User can edit an existing bookmark

	Scenario: Bookmark is added and user can edit it
		Given the book "The Three-Body Problem" by "Cixin Liu" has been added
		When command "edit" is selected
		And book ID 1 to edit is given
		And edit title "new Title", author "", isbn "978-7-5366-9293-2", description "Another good book", comment "New Comment" and tags "drama, scifi" are given
		Then system will respond with "Entry updated"
		And book entry ID 1 has title "new Title", author "Cixin Liu", isbn "978-7-5366-9293-2", description "Another good book", comment "New Comment" and tags "drama, scifi"

	Scenario: User gives negative integer on entry to edit
		When command "edit" is selected
		And book ID "-1" to edit is given
		Then system will respond with "Invalid entry ID"

	Scenario: User gives negative integer on entry to edit
		When command "edit" is selected
		And book ID "foo" to edit is given
		Then system will respond with "Invalid entry ID"
