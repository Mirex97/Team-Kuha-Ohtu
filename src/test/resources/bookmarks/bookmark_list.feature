Feature: User can view list of bookmarks

	Scenario: User can view list of bookmarks
		Given the book "The No. 1 Ladies' Detective Agency" by "Alexander McCall Smith" has been added
		When command list is selected
		Then system will respond with '1. "The No. 1 Ladies' Detective Agency" by Alexander McCall Smith'
