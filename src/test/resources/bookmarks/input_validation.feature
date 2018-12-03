Feature: When user inputs invalid values error message is displayed
	
	Scenario: User inputs invalid ISBN
		When command "add" is selected
		When type "book" is given
		Then system will respond with "Title: "
		When user types "Northern Lights"
		Then system will respond with "Author: "
		When user types "Philip Pullman"
		Then system will respond with "ISBN: "
		When user types "8743254sd54"
		Then system will respond with '"8743254sd54" is not a valid ISBN'
		And system will respond with "ISBN: "
		When user types "978-0-590-54178-7"
		Then system will respond with "Description: "
		When user types ""
		Then system will respond with "Comment: "
		When user types ""
		Then system will respond with "Tags: "
		When user types ""
		Then system will respond with "Entry created"

	Scenario: User inputs wrong link
		When command "add" is selected
		When type "video" is given
		Then system will respond with "Title: "
		When user types "Subscribe to Pewdiepie"
		Then system will respond with "Author: "
		When user types "Pewdiepie"
		Then system will respond with "Link: "
		When user types "test/youtube.com"
		Then system will respond with '"test/youtube.com" is not a valid Link'
		And system will respond with "Link: "
		When user types "https://youtube.com/watch?v=jokukivavideo6876432"
		Then system will respond with "Description: "
		When user types "Good video"
		Then system will respond with "Comment: "
		When user types "Test comment"
		Then system will respond with "Tags: "
		When user types ""
		Then system will respond with "Entry created"