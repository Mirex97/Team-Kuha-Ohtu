Feature: User can view bookmarks

	Scenario: User can see list of bookmarks
		Given the book "The No. 1 Ladies' Detective Agency" by "Alexander McCall Smith" has been added
		When command "list" is selected
		Then system will respond with 'Listing entries...'
		And system will respond with '1. book: "The No. 1 Ladies' Detective Agency" by Alexander McCall Smith'

	Scenario: User can view a specific bookmark
		Given the book "Stargate Atlantis: Homecoming" by "Jo Graham and Melissa Scott" with ISBN "978-1905586509", description "First post-series Atlantis novel" and tags "scifi, stargate" has been added
		When command "view" is selected
		And book ID 1 to view is given
		Then system will respond with 'Entry 1: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott'
		And system will respond with "ISBN: 978-1905586509"
		And system will respond with "Description: First post-series Atlantis novel"
		And system will respond with "Tags: scifi, stargate"
		And system will respond with "Is read? : False"
		And system will respond with "Want to mark it as read [y/N]? " 
		Then user types "y"
		Then system will respond with "Marked!"

	Scenario: Trying to view a non-existent bookmark ID produces error
		When command "view" is selected
		And book ID 123 to view is given
		Then system will respond with "Entry not found"

	Scenario: Trying to view an invalid bookmark ID produces error
		When command "view" is selected
		And book ID "foo" to view is given
		Then system will respond with "Invalid entry ID"

	Scenario: Trying to view a negative bookmark ID produces error
		When command "view" is selected
		And book ID "-123" to view is given
		Then system will respond with "Invalid entry ID"

        Scenario: User can mark bookmark as unread
                Given the book "Stargate Atlantis: Homecoming" by "Jo Graham and Melissa Scott" with ISBN "978-1905586509", description "First post-series Atlantis novel" and tags "scifi, stargate" has been added
                And command "view" is selected
                And book ID 1 to view is given
                And system will respond with 'Entry 1: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott'
		And system will respond with "ISBN: 978-1905586509"
		And system will respond with "Description: First post-series Atlantis novel"
		And system will respond with "Tags: scifi, stargate"
		And system will respond with "Is read? : False"
		And system will respond with "Want to mark it as read [y/N]? " 
                And user types "y"
                And system will respond with "Marked!"
                When command "view" is selected
                And book ID 1 to view is given
                And system will respond with 'Entry 1: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott'
		And system will respond with "ISBN: 978-1905586509"
		And system will respond with "Description: First post-series Atlantis novel"
		And system will respond with "Tags: scifi, stargate"
		And system will respond with "Is read? : True"
		And system will respond with "Want to mark it as unread [y/N]? " 
                Then user types "y"
                Then system will respond with "Unmarked!"

        Scenario: User can choose not to change read status of bookmark
                Given the book "Stargate Atlantis: Homecoming" by "Jo Graham and Melissa Scott" with ISBN "978-1905586509", description "First post-series Atlantis novel" and tags "scifi, stargate" has been added
                And command "view" is selected
                And book ID 1 to view is given
                And system will respond with 'Entry 1: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott'
		And system will respond with "ISBN: 978-1905586509"
		And system will respond with "Description: First post-series Atlantis novel"
		And system will respond with "Tags: scifi, stargate"
		And system will respond with "Is read? : False"
		And system will respond with "Want to mark it as read [y/N]? " 
                When user types "n"
                And command "list-u" is selected
                Then system will respond with "Listing unread entries..."
                Then system will respond with "1. book: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott"