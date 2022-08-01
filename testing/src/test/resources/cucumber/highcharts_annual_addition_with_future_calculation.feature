Feature: Highcharts Annual Addition With Future Calculation

  Scenario:
  If the solar potential of the city and the target year are given,
  the logistic function (S-function) is used to calculate the necessary additions of solar installations
  to have fully used the solar potential by the target year.

    Given is, that the Marktstammdatenregister for the postcode 60314 responds with
      | Id | Ort       | Plz   | EnergietraegerName       | Bruttoleistung | Nettonennleistung | InbetriebnahmeDatum   | BetriebsStatusName | DatumLetzteAktualisierung |
      | 1  | Frankfurt | 60314 | Solare Strahlungsenergie | 500.0          | 500.0             | /Date(1577836800000)/ | In Betrieb         | /Date(1577836800000)/     |
      | 2  | Frankfurt | 60314 | Solare Strahlungsenergie | 500.0          | 500.0             | /Date(1577836800000)/ | In Betrieb         | /Date(1577836800000)/     |

    And that the Marktstammdatenregister for the postcode 60528 responds with
      | Id | Ort       | Plz   | EnergietraegerName       | Bruttoleistung | Nettonennleistung | InbetriebnahmeDatum   | BetriebsStatusName | DatumLetzteAktualisierung |
      | 3  | Frankfurt | 60528 | Solare Strahlungsenergie | 500.0          | 500.0             | /Date(1577836800000)/ | In Betrieb         | /Date(1577836800000)/     |
      | 4  | Frankfurt | 60528 | Solare Strahlungsenergie | 500.0          | 500.0             | /Date(1640995200000)/ | In Betrieb         | /Date(1640995200000)/     |

    And the total solar potential of the city is 170.0 MWp
    And the target year is 2030

    When all solar systems in 'Frankfurt' with the following postcodes are imported from the Marktstammdatenregister
      | 60314 |
      | 60528 |

    Then the created highcharts for the annual addition of solar installation contains the following values
      | year | MWp   | number of solar systems |
      | 2020 | 1.50  | 3                       |
      | 2022 | 0.5   | 1                       |
      | 2023 | 2.61  |                         |
      | 2024 | 6.81  |                         |
      | 2025 | 14.92 |                         |
      | 2026 | 25.04 |                         |
      | 2027 | 31.33 |                         |
      | 2028 | 34.14 |                         |
      | 2029 | 34.69 |                         |
      | 2030 | 18.46 |                         |
