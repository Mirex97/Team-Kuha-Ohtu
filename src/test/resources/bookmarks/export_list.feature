Feature: User can export list

        Scenario: User can see list of bookmarks then export to list
            Given the book "The No. 1 Ladies Detective Agency" by "Alexander McCall Smith" has been added
            When command "l" is selected
            Then system will respond with "List only unread entries [y/N]? "
            And user types "n"
            And system will respond with "Entries:"
            Then system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'
            When command "x" is selected
            And system will respond with "File to export to: "
            And user types "list"
            Then system will respond with "Export successful"