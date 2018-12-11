Feature: User can search tags

	Scenario: Tag is added and user can search it
		When command "add" is selected
		And type "book" is given
		And title "The Empire Strikes Back", author "Donald F. Glut", isbn "978-0-345-28392-4", description "Based on the actual script.", comment "Yoda is blue!" and tags "scifi, fantasy" are given
		Then system will respond with "Entry created"
		Then user has entered tag section
		When tag command "find" is selected
		And tag find query "fantasy" is given
		Then system will respond with "1 match"
		And system will respond with '1. book: "The Empire Strikes Back" by Donald F. Glut'

        Scenario: User searches for nonexisting tag
                Then user has entered tag section
		When tag command "find" is selected
		And tag find query "fantasy" is given
		Then system will respond with "No matches with tag :("

        Scenario: User enter unknown command
                Then user has entered tag section
		When tag command "fond" is selected
		Then system will respond with "Unknown command. Type "help" for help."
