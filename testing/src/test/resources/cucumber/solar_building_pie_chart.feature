Feature: solar building pie chart

  Scenario:
  All solar systems installed in a city or region are registered in the Marktstammdatenregister.
  The data is publicly available there and can be downloaded by post code to create a pie chart
  showing the distribution on different building categories.
  The post codes assigned to a city are resolved via Opendatasoft.

  This test is also a performance test.

    Given is, that for the postcode search for 'Frankfurt', the following postcodes are stored in Opendatasoft
      | 60314 |
    And that in 2022 for postcode 60314, 40000 homes (with 5.0 kWp)
    And 12000 solar systems on apartment buildings (with 25.0 kWp)
    And 4600 solar systems on commercial buildings (with 100.0 kWp)
    And 400 on schools (with 100.0 kWp) are registered in the Marktstammdatenregister

    When all solar systems in 'Frankfurt' are downloaded from the Marktstammdatenregister

    Then the pie chart must have a slice with 40000 solar installations with 200 MWp, which is 20 %
    And a slice with 12000 solar installations with 300 MWp, which is 30 %
    And a slice with 5000 solar installations with 500 MWp, which is 50 %
