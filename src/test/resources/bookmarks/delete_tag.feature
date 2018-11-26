Feature: User can delete existing tags
	Scenario: Tag is added and user can delete it
		Given the book "Le book" by "the le book author" with ISBN "12321", description "none" and tags "taggies" has been added
		When command "tags" is selected
		Then system will respond with the tag help page
		When tag section command "delete" is selected
		And tag ID 1 and confirmation "y" to delete is given
		Then system will respond with "Tag deleted successfully"
		When tag section command "list" is selected
		Then system will respond with "No tags"
		When tag section command "return" is selected

	Scenario: Tag is added and user cancels deletion
		Given the book "Le book" by "the le book author" with ISBN "12321", description "none" and tags "taggies" has been added
		When command "tags" is selected
		Then system will respond with the tag help page
		When tag section command "delete" is selected
		And tag ID 1 and confirmation "n" to delete is given
		Then system will respond with "Deletion cancelled"
		When tag section command "return" is selected