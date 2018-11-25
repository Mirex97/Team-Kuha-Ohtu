Feature: User can delete existing bookmarks

	Scenario: Bookmark is added and user can delete it
		Given the book "The Three-Body Problem" by "Cixin Liu" has been added
		When command delete is selected
		And book ID 1 and confirmation "y" to delete is given
		Then system will respond with "Entry deleted successfully."
		When command list is selected
		Then system will respond with "No entries :("

	Scenario: Bookmark is added and user cancels the deletion
		Given the book "The Three-Body Problem" by "Cixin Liu" has been added
		When command delete is selected
		And book ID 1 and confirmation "N" to delete is given
		Then system will respond with "Deletion cancelled"