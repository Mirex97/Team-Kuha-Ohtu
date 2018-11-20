Feature: User can list help

  Scenario: User can list help
    Given command help is selected
    When application is run
    Then system will respond with "add: add a new entry"

#  Scenario: User can list help
#    Given new application is run
#    Then system will respond with "Anna komento:"
