Feature: User can search tags

	Scenario: Tag is added and user can search it
		Given the book "Le book" by "the le book author" with ISBN "12321", description "none" and tags "taggies" has been added
		And user has entered tag section
		When tag command "find" is selected
		And tag find query "taggies" is given
		Then system will respond with "1 match"
		And system will respond with '1. book: "Le book" by the le book author'
