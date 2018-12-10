Feature: User can use shortcuts

        Scenario: User can add a new bookmark using shortcut command
            When command "a" is selected
            And type "book" is given
            And title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-7", description "Good fantasy book", comment "" and tags "fantasy, steampunk" are given
            Then system will respond with "Entry created"
            And book entry ID 1 has title "Northern Lights", author "Philip Pullman", isbn "978-0-590-54178-7", description "Good fantasy book", comment "" and tags "fantasy, steampunk"

        Scenario: Bookmark is added and user can edit it using shortcut command
            Given the book "The Three-Body Problem" by "Cixin Liu" has been added
            When command "e" is selected
            And book ID 1 to edit is given
            And edit title "new Title", author "", isbn "978-7-5366-9293-0", description "Another good book", comment "New Comment" and tags "drama, scifi" are given
            Then system will respond with "Entry updated"
            And book entry ID 1 has title "new Title", author "Cixin Liu", isbn "978-7-5366-9293-0", description "Another good book", comment "New Comment" and tags "drama, scifi"

        Scenario: Bookmark is added and user can delete it using shortcut command
            Given the book "Nineteen Eighty-Four" by "George Orwell" has been added
            When command "d" is selected
            And book ID 1 and confirmation "y" to delete is given
            Then system will respond with "Entry deleted successfully."
            When command "list" is selected
            Then system will respond with "List only unread entries [y/N]? "
            When user types "n"
            Then system will respond with "No entries :("

        Scenario: User can mark bookmark as unread using shortcut commands
            Given the book "Stargate Atlantis: Secrets" by "Jo Graham and Melissa Scott" with ISBN "978-1905586592", description "Fifth novel in the Legacy series" and tags "scifi, stargate" has been added
            When command "r" is selected
            And book ID 1 to mark as read is given
            Then system will respond with "Entry marked as read"
            When command "u" is selected
            And book ID 1 to mark as unread is given
            Then system will respond with "Entry marked as unread"

        Scenario: User can view a specific bookmark using shortcut command
            Given the book "Stargate Atlantis: Homecoming" by "Jo Graham and Melissa Scott" with ISBN "978-1905586509", description "First post-series Atlantis novel" and tags "scifi, stargate" has been added
            When command "v" is selected
            And book ID 1 to view is given
            Then system will respond with 'Entry 1: "Stargate Atlantis: Homecoming" by Jo Graham and Melissa Scott'
            And system will respond with "ISBN: 978-1905586509"
            And system will respond with "Description: First post-series Atlantis novel"
            And system will respond with "Tags: scifi, stargate"
            And system will respond with "Is read: No"

        Scenario: User can see list of bookmarks using shortcut command
            Given the book "The No. 1 Ladies Detective Agency" by "Alexander McCall Smith" has been added
            When command "l" is selected
            Then system will respond with "List only unread entries [y/N]? "
            When user types "n"
            Then system will respond with "Entries:"
            And system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'

        Scenario: The user can find bookmarks from many bookmarks that have been added using shortcut command
            Given the book "Stargate Atlantis: Homecoming" by "Jo Graham and Melissa Scott" has been added
            And the book "The No. 1 Ladies' Detective Agency" by "Alexander McCall Smith" has been added
            And the book "Leviathan Wakes" by "James S. A. Corey" has been added
            When command "f" is selected
            And search query "Corey" is given
            Then system will respond with "Match:"
            And system will respond with '3. book: "Leviathan Wakes" by James S. A. Corey'

        Scenario: User can type help to get command list using shortcut command
            When command "h" is selected
            Then system will respond with the main help page

        Scenario: User can type i to get intro using shortcut command
            When command "i" is selected
            Then system will respond with the introduction
