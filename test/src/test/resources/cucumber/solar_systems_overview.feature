Feature: solar system overview

  Scenario:
  All solar systems installed in a city or region are registered in the Marktstammdatenregister.
  The data is publicly available there and can be downloaded by post code to create a solar overview of a city.
  The post codes assigned to a city are resolved via Opendatasoft.

  This feature is used to update the introduction text on pv-frankfurt.de
  so that the renewable state of play when people visit the site is always up to date.

  Note:
  Decommissioned solar systems may not be included in the overview.

    Given is, that for the postcode search for 'Frankfurt', the following postcodes are stored in Opendatasoft
      | 60314 |
      | 60528 |

    And that for the postcode 60314, the following solar systems are registered in the Marktstammdatenregister
      | Id | Ort       | Plz   | Bruttoleistung | Nettonennleistung | Inbetriebnahme | BetriebsStatus        | DatumLetzteAktualisierung |
      | 1  | Frankfurt | 60314 | 4.2            | 4.2               | 2020-01-01     | In Betrieb            | 2020-01-01T00:00:00Z      |
      | 2  | Frankfurt | 60314 | 17.1           | 17.1              | 2020-01-01     | Permanent stillgelegt | 2020-01-01T00:00:00Z      |

    And that for the postcode 60528, the following solar systems are registered in the Marktstammdatenregister
      | Id | Ort       | Plz   | Bruttoleistung | Nettonennleistung | Inbetriebnahme | BetriebsStatus | DatumLetzteAktualisierung |
      | 3  | Frankfurt | 60528 | 25.9           | 25.9              | 2020-01-01     | In Betrieb     | 2020-01-01T00:00:00Z      |
      | 4  | Frankfurt | 60528 | 9.1            | 9.1               | 2022-01-01     | In Betrieb     | 2022-01-01T00:00:00Z      |

    When all solar systems in 'Frankfurt' are downloaded from the Marktstammdatenregister
    Then the overview has a total of 3 installed solar systems with a capacity of 0.04 megawatt peak


  Scenario:
  Balkonkraftwerke need to be filtered out for the overview,
  as the solar potential of the city relates to rooftops.

    Given is, that the Gemeindeschlüssel for 'Frankfurt' is '06412000'

    And that in 2022 in 'Frankfurt' with Gemeindeschlüssel '06412000', 1000 Balkonkraftwerke (with 0.6 kWp)
    And 400 solar systems on apartment buildings (with 25.0 kWp)
    And 100 on schools (with 100.0 kWp) are registered in the Marktstammdatenregister

    When all solar systems are downloaded from the Marktstammdatenregister
    Then the overview has a total of 500 installed solar systems with a capacity of 20.0 megawatt peak
