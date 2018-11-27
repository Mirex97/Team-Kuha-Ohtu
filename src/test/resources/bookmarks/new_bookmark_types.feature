Feature: User can add a new bookmark of a different type

	Scenario: User can add an video bookmark
		When command "add" is selected
		And type "video" is given
		And title "Merge sort algorithm", author "mycodeschool", link "https://www.youtube.com/watch?v=TzeBrDU-JaY", description "", comment "Hyvä selitys merge sortin toiminnasta esimerkin avulla" and tags "" are given
		Then system will respond with "Entry created"
		And video entry ID 1 has title "Merge sort algorithm", author "mycodeschool", link "https://www.youtube.com/watch?v=TzeBrDU-JaY", description "", comment "Hyvä selitys merge sortin toiminnasta esimerkin avulla" and tags ""

	Scenario: User can add a blogpost bookmark
		When command "add" is selected
		And type "blog" is given
		And title "Consistency models", author "Nicola Apicella", link "https://dev.to/napicellatwit/consistency-models-52l", description "", comment "" and tags "" are given
		Then system will respond with "Entry created"
		And blog entry ID 1 has title "Consistency models", author "Nicola Apicella", link "https://dev.to/napicellatwit/consistency-models-52l", description "", comment "" and tags ""

	Scenario: User can add a podcast bookmark
		When command "add" is selected
		And type "podcast" is given
		And title "Jim Benson on Personal Kanban, Lean Coffee and collaboration", author "Sami Honkonen", podcast name "Boss Level Podcast", description "Personal Kanban, which is an approach to dealing with the overload of stuff you need to deal with. We dig into into its two simple rules, visualizing work and limiting work in progress. We then walk through Lean Coffee, which is a simple and effective way to run your meetings.", comment "" and tags "" are given
		Then system will respond with "Entry created"

        Scenario: User can add a meme bookmark
		When command "add" is selected
		And type "meme" is given
		And title "No beep", author "myself", image "Good guy greg", up text "Microwaving at midnight", bottom text "Opens it with one sec left", comment "" and tags "" are given
		Then system will respond with "Entry created"
#		And meme entry ID 1 has title "No beep", author "myself", image "Good guy greg", up text "Microwaving at midnight", bottom text "Opens it with one sec left" and comment ""

        Scenario: User can add an article bookmark
		When command "add" is selected
		And type "article" is given
		And title "Article", author "auto-thor", paper "The good one", description "Mildly wild", comments "Keep an eye on this" and tags "" are given
		Then system will respond with "Entry created"

        Scenario: User can add an article bookmark
		When command "add" is selected
		And type "book" is given
		And title "Book", author "book-t", isbn "15617", description "Wild", comments "Cant keep an eye open on this" and tags "" are given
		Then system will respond with "Entry created"