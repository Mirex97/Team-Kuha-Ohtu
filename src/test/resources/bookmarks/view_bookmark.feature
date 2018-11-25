Feature: User can view bookmarks

	Scenario: User can see list of bookmarks
		Given the book "The No. 1 Ladies' Detective Agency" by "Alexander McCall Smith" has been added
		When command list is selected
		Then system will respond with '1. "The No. 1 Ladies' Detective Agency" by Alexander McCall Smith'

	Scenario: User can view a specific bookmark
		Given the book "Stargate Atlantis: Homecoming" by "Jo Graham and Melissa Scott" has been added
		When command view is selected
		And book ID 1 is given
		Then system will respond with 'Entry 1: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott'
