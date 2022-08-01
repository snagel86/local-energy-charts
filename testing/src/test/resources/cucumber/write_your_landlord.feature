Feature: Write Your Landlord

  Scenario:
  In order for cities to become more renewable locally, and less dependent on fossil imports,
  it first needs building owners to lease their roof to an energy cooperative.
  That's why pv-frankfurt.de has a campaign called "write-your-landlord".

    When the write-your-landlord page has been opened
    And the write-your-landlord page has been reopened
    And the write-your-landlord page has been reopened again
    And the tenant writes the following from "me@tenant.de" to the landlord to "my@landlord.de"
    """
    Please lease your roof to an energy cooperative!

    Thank you & best regards
    Your tenant
    """
    Then statistically the page must have been opened 3 times, and an email must have been sent 1 time