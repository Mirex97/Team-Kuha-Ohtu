Feature: When user inputs invalid values error message is displayed
	
	Scenario: User inputs invalid ISBN
		When command "add" is selected
		And type "book" is given
		And user types "Northern Lights" into field "Title: "
		And user types "Philip Pullman" into field "Author: "
		And user types "8743254sd54" into field "ISBN: "
		Then system will respond with '"8743254sd54" is not a valid ISBN'
		When user types "978-0-590-54178-7" into field "ISBN: "
		And user types "" into field "Description: "
		And user types "" into field "Comment: "
		And user types "" into field "Tags: "
		Then system will respond with "Entry created"

	Scenario: User inputs invalid ISBN in old format
		When command "add" is selected
		And type "book" is given
		And user types "Northern Lights" into field "Title: "
		And user types "Philip Pullman" into field "Author: "
		And user types "0-306-40j15-2" into field "ISBN: "
		Then system will respond with '"0-306-40j15-2" is not a valid ISBN'
		When user types "0-306-40615-2" into field "ISBN: "
		And user types "" into field "Description: "
		And user types "" into field "Comment: "
		And user types "" into field "Tags: "
		Then system will respond with "Entry created"

	Scenario: User inputs wrong link
		When command "add" is selected
		And type "video" is given
		And user types "Subscribe to Pewdiepie" into field "Title: "
		And user types "Pewdiepie" into field "Author: "
		And user types "test/youtube.com" into field "Link: "
		Then system will respond with '"test/youtube.com" is not a valid Link'
		When user types "https://youtube.com/watch?v=jokukivavideo6876432" into field "Link: "
		And user types "Good video" into field "Description: "
		And user types "Test comment" into field "Comment: "
		And user types "" into field "Tags: "
		Then system will respond with "Entry created"