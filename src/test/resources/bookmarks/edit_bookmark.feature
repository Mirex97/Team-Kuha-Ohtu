Feature: User can edit an existing bookmark

	Scenario: Bookmark is added and user can edit it
		Given the book "The Three-Body Problem" by "Cixin Liu" has been added
		When command edit is selected
		And book ID 1 to edit is given
		And edit title "new Title", author "", isbn "978-7-5366-9293-2", description "Another good book", comment "New Comment" and tags "Drama" are given
		Then system will respond with "Entry updated"
