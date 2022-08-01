Feature: Solar System Overview

  Scenario:
  Solar installations from the Frankfurt districts Ostend and Sachsenhausen are requested and saved
  by the Marktstammdatenregister. Then an overview of the solar city Frankfurt is requested.
  Decommissioned solar systems are not taken into account.

    Given is, that the Marktstammdatenregister for the postcode 60314 responds with
      | Id | Ort       | Plz   | EnergietraegerName       | Bruttoleistung | Nettonennleistung | InbetriebnahmeDatum   | BetriebsStatusName    | DatumLetzteAktualisierung |
      | 1  | Frankfurt | 60314 | Solare Strahlungsenergie | 4.2            | 4.2               | /Date(1577836800000)/ | In Betrieb            | /Date(1577836800000)/     |
      | 2  | Frankfurt | 60314 | Solare Strahlungsenergie | 17.1           | 17.1              | /Date(1577836800000)/ | Permanent stillgelegt | /Date(1577836800000)/     |

    And that the Marktstammdatenregister for the postcode 60528 responds with
      | Id | Ort       | Plz   | EnergietraegerName       | Bruttoleistung | Nettonennleistung | InbetriebnahmeDatum   | BetriebsStatusName | DatumLetzteAktualisierung |
      | 3  | Frankfurt | 60528 | Solare Strahlungsenergie | 25.9           | 25.9              | /Date(1577836800000)/ | In Betrieb         | /Date(1577836800000)/     |
      | 4  | Frankfurt | 60528 | Solare Strahlungsenergie | 9.1            | 9.1               | /Date(1640995200000)/ | In Betrieb         | /Date(1640995200000)/     |

    When all solar systems in 'Frankfurt' with the following postcodes are imported from the Marktstammdatenregister
      | 60314 |
      | 60528 |
    Then the overview has a total of 3 installed solar systems with a capacity of 0.04 megawatt peak
