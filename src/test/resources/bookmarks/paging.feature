Feature: User can view lists devided in pages

        Scenario: User can see list by page using command
            Given the book "The No. 1 Ladies Detective Agency" by "Alexander McCall Smith" has been added
            When command "list" is selected
            Then system will respond with "List only unread entries [y/N]? "
            When user types "n"
            Then system will respond with "Entries:"
            And system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'
            When command "page" is selected
            Then system will respond with "Page to show: "
            And user types "1"
            Then system will respond with "Page 1 out of 1:"
            And system will respond with '1. book: "The No. 1 Ladies Detective Agency" by Alexander McCall Smith'

        Scenario: User is given error when giving negative integer for page
            Given command "page" is selected
            Then system will respond with "Page to show: "
            And user types "-1"
            Then system will respond with "Please give a positive index."
            

        Scenario: User tries to view a page without creating a list
            Given command "page" is selected
            Then system will respond with "Page to show: "
            And user types "1"
            Then system will respond with "Nothing to show"