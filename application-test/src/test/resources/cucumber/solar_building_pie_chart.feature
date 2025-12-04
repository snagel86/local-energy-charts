Feature: solar building pie chart

  Scenario:
  All solar installations in a city are registered in the Marktstammdatenregister.
  The data is publicly accessible there, allowing us
  to create a solar city based on the Marktstammdatenregister to create a pie chart
  showing the distribution on different building categories.

  This test is also a performance test.

    Given is, that the Gemeindeschlüssel for 'Frankfurt' is '06412000'
    And that in 2022 in 'Frankfurt' with Gemeindeschlüssel '06412000', 4000 homes (with 5.0 kWp)
    And 1200 solar systems on apartment buildings (with 25.0 kWp)
    And 460 solar systems on commercial buildings (with 100.0 kWp)
    And 40 on schools (with 100.0 kWp) are registered in the Marktstammdatenregister

    When the solar city, 'Frankfurt', is created based on the Marktstammdatenregister

    Then the pie chart must have a slice with 4000 solar installations with 20 MWp, which is 20 %
    And a slice with 1200 solar installations with 30 MWp, which is 30 %
    And a slice with 500 solar installations with 50 MWp, which is 50 %
