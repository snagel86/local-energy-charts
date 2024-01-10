Feature: now we need roofs!

  Scenario:
  Most people in Frankfurt live for rent. But while tenants would benefit
  from more independent, cheaper solar power, 'Mieterstrom' is doing poorly.
  That's why there's a campaign on pv-frankfurt.de for tenants to ask their
  landladies*lords to lease the roof to an energy cooperative.

    When the tenant writes the following from "me@tenant.de" to the landlady*lord to "my@landlady.de"
    """
    Please lease your roof to an energy cooperative!

    Thank you & best regards
    Your tenant
    """
    Then the mail was successfully sent.