Feature: User can add a new bookmark of a different type

	Scenario: User can add an article bookmark
		When command add is selected
		And type "video" is given
		And title "Merge sort algorithm", author "mycodeschool", link "https://www.youtube.com/watch?v=TzeBrDU-JaY", description "", comment "Hyvä selitys merge sortin toiminnasta esimerkin avulla" and tags "" are given
		Then system will respond with "Entry created"
		And video entry ID 1 has title "Merge sort algorithm", author "mycodeschool", link "https://www.youtube.com/watch?v=TzeBrDU-JaY", description "", comment "Hyvä selitys merge sortin toiminnasta esimerkin avulla" and tags ""
