Feature: User can edit an existing bookmark

	Scenario: Bookmark is added and user can edit it
		Given command add is selected
		When type "book", title "The Three-Body Problem", author "Cixin Liu", isbn "978-7-5366-9293-0", description "Good science fiction book", comment "BookmarkEditTest" and tags "scifi" are given
		And command edit is selected
		When input "1" is given
		When edit title "new Title", author "", isbn "978-7-5366-9293-2", description "Another good book", comment "New Comment" and tags "Drama" are given
		Then system will respond with "Entry updated"