Feature: solar building pie chart

  Scenario:
  All solar systems installed in a city or region are registered in the Marktstammdatenregister.
  The data is publicly available there and can be downloaded by post code to create a pie chart
  showing the distribution on different building categories.
  The post codes assigned to a city are resolved via Opendatasoft.

  This test is also a performance test.

    Given is, that for the postcode search for 'Frankfurt', the following postcodes are stored in Opendatasoft
      | 60314 |
    And that in 2022 for postcode 60314, 4000 homes (with 5.0 kWp)
    And 1200 solar systems on apartment buildings (with 25.0 kWp)
    And 460 solar systems on commercial buildings (with 100.0 kWp)
    And 40 on schools (with 100.0 kWp) are registered in the Marktstammdatenregister

    When all solar systems in 'Frankfurt' are downloaded from the Marktstammdatenregister

    Then the pie chart must have a slice with 4000 solar installations with 20 MWp, which is 20 %
    And a slice with 1200 solar installations with 30 MWp, which is 30 %
    And a slice with 500 solar installations with 50 MWp, which is 50 %
