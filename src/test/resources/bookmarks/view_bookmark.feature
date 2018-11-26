Feature: User can view bookmarks

	Scenario: User can see list of bookmarks
		Given the book "The No. 1 Ladies' Detective Agency" by "Alexander McCall Smith" has been added
		When command "list" is selected
		Then system will respond with '1. "The No. 1 Ladies' Detective Agency" by Alexander McCall Smith'

	Scenario: User can view a specific bookmark
		Given the book "Stargate Atlantis: Homecoming" by "Jo Graham and Melissa Scott" with ISBN "978-1905586509", description "First post-series Atlantis novel" and tags "scifi, stargate" has been added
		When command "view" is selected
		And book ID 1 to view is given
		Then system will respond with 'Entry 1: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott'
		And system will respond with "ISBN: 978-1905586509"
		And system will respond with "Description: First post-series Atlantis novel"
		And system will respond with "Tags: scifi, stargate"

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
