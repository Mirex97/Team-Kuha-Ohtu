Feature: User can delete existing tags
	Scenario: Tag is added and user can delete it
		When command "add" is selected
		And type "book" is given
		And title "The Empire Strikes Back", author "Donald F. Glut", isbn "978-0-345-28392-4", description "Based on the actual script.", comment "Yoda is blue!" and tags "scifi" are given
		Then system will respond with "Entry created"
		And user has entered tag section
		When tag command "delete" is selected
		And tag ID 1 and confirmation "y" to delete is given
		Then system will respond with "Tag deleted successfully"
		When tag command "list" is selected
		Then system will respond with "No tags"

	Scenario: Tag is added and user cancels deletion
		When command "add" is selected
		And type "book" is given
		And title "The Empire Strikes Back", author "Donald F. Glut", isbn "978-0-345-28392-4", description "Based on the actual script.", comment "Yoda is blue!" and tags "scifi" are given
		Then system will respond with "Entry created"
		And user has entered tag section
		When tag command "delete" is selected
		And tag ID 1 and confirmation "n" to delete is given
		Then system will respond with "Deletion cancelled"
