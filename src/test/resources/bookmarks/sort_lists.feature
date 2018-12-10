Feature: User can sort lists
	
	Scenario: User can sort a list using type and id
		Given the book "The No. 1 Ladies Detective Agency" by "Alexander McCall Smith" has been added
		And the book "In Search of Lost Time" by "Marcel Proust" has been added

		And command "add" is selected
		And type "video" is given
		And title "Merge sort algorithm", author "mycodeschool", link "https://www.youtube.com/watch?v=TzeBrDU-JaY", description "", comment "Hyvä selitys merge sortin toiminnasta esimerkin avulla" and tags "" are given
		Then system will respond with "Entry created"
		And video entry ID 3 has title "Merge sort algorithm", author "mycodeschool", link "https://www.youtube.com/watch?v=TzeBrDU-JaY", description "", comment "Hyvä selitys merge sortin toiminnasta esimerkin avulla" and tags ""

		When command "add" is selected
		And type "blog" is given
		And title "Consistency models", author "Nicola Apicella", link "https://dev.to/napicellatwit/consistency-models-52l", description "", comment "" and tags "" are given
		Then system will respond with "Entry created"
		And blog entry ID 4 has title "Consistency models", author "Nicola Apicella", link "https://dev.to/napicellatwit/consistency-models-52l", description "", comment "" and tags ""

		When command "list" is selected
		Then system will respond with "List only unread entries [y/N]? "
		When user types "n"
		Then system will respond with "Entries:"
		And system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'
		And system will respond with '2. book: "In Search of Lost Time" by Marcel Proust'
		And system will respond with '3. video: "Merge sort algorithm" by mycodeschool'
		And system will respond with '4. blog: "Consistency models" by Nicola Apicella'

		When command "sort" is selected
		Then system will respond with "1st parameter to sort by: "
		When user types "type"
		Then system will respond with "2nd parameter to sort by: "
		When user types "Title"
		Then system will respond with "3rd parameter to sort by: "
		When user types "Id"
		Then system will respond with "4th parameter to sort by: "
		When user types ""
		Then system will respond with "Entries:"
		And system will respond with '4. blog: "Consistency models" by Nicola Apicella'
		And system will respond with '2. book: "In Search of Lost Time" by Marcel Proust'
		And system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'
		And system will respond with '3. video: "Merge sort algorithm" by mycodeschool'
		
	Scenario: User can sort a long list of entries
		Given the book "The No. 1 Ladies Detective Agency" by "Alexander McCall Smith" has been added
		And the book "In Search of Lost Time" by "Marcel Proust" has been added
		And the book "Don Quixote" by "Miguel de Cervantes" has been added
		And the book "Ulysses" by "James Joyce" has been added
		And the book "The Great Gatsby" by "F. Scott Fitzgerald" has been added
		And the book "Moby Dick" by "Herman Melville" has been added
		And the book "Hamlet" by "William Shakespeare" has been added
		And the book "War and Peace" by "Leo Tolstoy" has been added
		And the book "The Odyssey" by "Homer" has been added
		And the book "One Hundred Years of Solitude" by "Gabriel Garcia Marquez" has been added
		And the book "The Divine Comedy" by "Dante Alighieri" has been added
		And the book "The Brothers Karamazov" by "Fyodor Dostoyevsky" has been added
		
		When command "list" is selected
		Then system will respond with "List only unread entries [y/N]? "
		When user types "n"
		Then system will respond with "Page 1 out of 2:"
		And system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'
		And system will respond with '2. book: "In Search of Lost Time" by Marcel Proust'
		And system will respond with '3. book: "Don Quixote" by Miguel de Cervantes'
		And system will respond with '4. book: "Ulysses" by James Joyce'
		And system will respond with '5. book: "The Great Gatsby" by F. Scott Fitzgerald'
		And system will respond with '6. book: "Moby Dick" by Herman Melville'
		And system will respond with '7. book: "Hamlet" by William Shakespeare'
		And system will respond with '8. book: "War and Peace" by Leo Tolstoy'
		And system will respond with '9. book: "The Odyssey" by Homer'
		And system will respond with '10. book: "One Hundred Years of Solitude" by Gabriel Garcia Marquez'
		When command "next" is selected
		Then system will respond with "Page 2 out of 2:"
		And system will respond with '11. book: "The Divine Comedy" by Dante Alighieri'
		And system will respond with '12. book: "The Brothers Karamazov" by Fyodor Dostoyevsky'

		When command "sort" is selected
		Then system will respond with "1st parameter to sort by: "
		When user types "Title"
		Then system will respond with "2nd parameter to sort by: "
		When user types "Author"
		Then system will respond with "3rd parameter to sort by: "
		When user types "Id"
		Then system will respond with "4th parameter to sort by: "
		When user types ""

		Then system will respond with "Page 1 out of 2:"
		And system will respond with '3. book: "Don Quixote" by Miguel de Cervantes'
		And system will respond with '7. book: "Hamlet" by William Shakespeare'
		And system will respond with '2. book: "In Search of Lost Time" by Marcel Proust'
		And system will respond with '6. book: "Moby Dick" by Herman Melville'
		And system will respond with '10. book: "One Hundred Years of Solitude" by Gabriel Garcia Marquez'
		And system will respond with '12. book: "The Brothers Karamazov" by Fyodor Dostoyevsky'
		And system will respond with '11. book: "The Divine Comedy" by Dante Alighieri'
		And system will respond with '5. book: "The Great Gatsby" by F. Scott Fitzgerald'
		And system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'
		And system will respond with '9. book: "The Odyssey" by Homer'
		
		When command "next" is selected
		Then system will respond with "Page 2 out of 2:"
		And system will respond with '4. book: "Ulysses" by James Joyce'
		And system will respond with '8. book: "War and Peace" by Leo Tolstoy'
		
		
		
		