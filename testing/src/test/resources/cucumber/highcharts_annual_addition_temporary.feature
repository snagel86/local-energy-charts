Feature: Highcharts Annual Addition Temporary

  Scenario:
  Installed solar systems are registered in the Marktstammdatenregister.
  Solar systems installed in a city can then be loaded via a technical interface
  to calculate the installation additions of solar systems and requested for visualization with Highcharts.
  Since the data is not stored in a database in advance, and is automatically updated nightly,
  this request is slower, but does not require access rights to the database.

    Given is, that the Marktstammdatenregister for the postcode 60314 responds with
      | Id | Ort       | Plz   | EnergietraegerName       | Bruttoleistung | Nettonennleistung | InbetriebnahmeDatum   | BetriebsStatusName | DatumLetzteAktualisierung |
      | 1  | Frankfurt | 60314 | Solare Strahlungsenergie | 4.2            | 4.2               | /Date(1577836800000)/ | In Betrieb         | /Date(1577836800000)/     |
      | 2  | Frankfurt | 60314 | Solare Strahlungsenergie | 17.1           | 17.1              | /Date(1577836800000)/ | In Betrieb         | /Date(1577836800000)/     |

    And that the Marktstammdatenregister for the postcode 60528 responds with
      | Id | Ort       | Plz   | EnergietraegerName       | Bruttoleistung | Nettonennleistung | InbetriebnahmeDatum   | BetriebsStatusName | DatumLetzteAktualisierung |
      | 3  | Frankfurt | 60528 | Solare Strahlungsenergie | 25.9           | 25.9              | /Date(1577836800000)/ | In Betrieb         | /Date(1577836800000)/     |
      | 4  | Frankfurt | 60528 | Solare Strahlungsenergie | 7.1            | 7.1               | /Date(1640995200000)/ | In Betrieb         | /Date(1640995200000)/     |

    When all solar systems in 'Frankfurt' with the following postcodes are temporary requested from the Marktstammdatenregister
      | 60314 |
      | 60528 |
    
    Then the created highcharts for the temporary annual rate of solar installation contains the following values
      | year | MWp  | number of solar systems |
      | 2020 | 0.05 | 3                       |
      | 2022 | 0.01 | 1                       |
