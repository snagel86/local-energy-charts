Feature: annual addition of solar installations (with highcharts)

  Scenario:
  All solar installations in a city are registered in the Marktstammdatenregister.
  The data is publicly accessible there, allowing us
  to create a solar city based on the Marktstammdatenregister for statistical calculations.
  The annual additions are calculated and displayed via Highcharts in the web browser.

  The solar installations are requested by post code in the Marktstammdatenregister.
  The post codes assigned to a city are resolved via Opendatasoft, so that only the name of the city is required.

  Note:
  Solar installations that have now been permanently decommissioned (see id = 3)
  are included in the annual additions because they were installed at the time of commissioning.

  See:
  Marktstammdatenregister: (https://www.marktstammdatenregister.de/MaStR)
  Opendatasoft: (https://public.opendatasoft.com/explore/dataset/georef-germany-postleitzahl/information/)
  Highcharts: (https://www.highcharts.com/)

    Given is, that for the postcode search for 'Frankfurt', the following postcodes are stored in Opendatasoft
      | 60314 |
      | 60528 |

    And that for the postcode 60314, the following solar systems are registered in the Marktstammdatenregister
      | Id | Ort       | Plz   | Nettonennleistung | Inbetriebnahme | BetriebsStatus | DatumLetzteAktualisierung |
      | 1  | Frankfurt | 60314 | 20.0              | 2020-01-01     | In Betrieb     | 2020-01-01T00:00:00Z      |
      | 2  | Frankfurt | 60314 | 40.0              | 2020-01-01     | In Betrieb     | 2020-01-01T00:00:00Z      |

    And that for the postcode 60528, the following solar systems are registered in the Marktstammdatenregister
      | Id | Ort       | Plz   | Nettonennleistung | Inbetriebnahme | BetriebsStatus        | DatumLetzteAktualisierung |
      | 3  | Frankfurt | 60528 | 40.0              | 2018-01-01     | Permanent stillgelegt | 2022-01-01T00:00:00Z      |
      | 4  | Frankfurt | 60528 | 10.0              | 2022-01-01     | In Betrieb            | 2022-01-01T00:00:00Z      |
      | 5  | Frankfurt | 60528 | 40.0              | 2022-01-01     | In Betrieb            | 2022-01-01T00:00:00Z      |

    When the solar city, 'Frankfurt', is created based on the Marktstammdatenregister

    Then the calculated highchart has a total of 0.15 MWp and contains the following values
      | year | MWp  | number of solar systems |
      | 2018 | 0.04 | 1                       |
      | 2020 | 0.06 | 2                       |
      | 2022 | 0.05 | 2                       |


  Scenario:
  If the solar potential of the city and the target year are given,
  the logistic function (S-function) is used to calculate the necessary additions of solar installations
  to have fully used the solar potential by the target year.

  In this scenario, we use the Gemeindeschlüssel to request the installed solar systems
  from the Marktstammdatenregister instead of post codes.

  See:
  https://www.statistikportal.de/de/gemeindeverzeichnis

  Notes:
  a)
  To calculate the necessary future addition (in MWp),
  previously installed solar systems are subtracted from the total solar potential of the city.
  However, permanently decommissioned solar systems (see id = 3) are not subtracted, as these roofs are free again.

  b)
  To calculate the future necessary number of solar systems per year,
  the average of all previously installed solar systems is scaled to the corresponding year.
  This also includes permanently decommissioned solar systems,
  since they were installed at the corresponding point in time and are therefore relevant for the average.

    Given is, that for the Gemeindeschlüssel '06412000', the following solar systems are registered in the Marktstammdatenregister
      | Id | Ort       | Plz   | Nettonennleistung | Inbetriebnahme | BetriebsStatus        | DatumLetzteAktualisierung |
      | 1  | Frankfurt | 60314 | 25.0              | 2020-01-01     | In Betrieb            | 2020-01-01T00:00:00Z      |
      | 2  | Frankfurt | 60314 | 25.0              | 2022-01-01     | In Betrieb            | 2020-01-01T00:00:00Z      |
      | 3  | Frankfurt | 60528 | 40.0              | 2018-01-01     | Permanent stillgelegt | 2020-01-01T00:00:00Z      |
      | 4  | Frankfurt | 60528 | 25.0              | 2020-01-01     | In Betrieb            | 2020-01-01T00:00:00Z      |
      | 5  | Frankfurt | 60528 | 25.0              | 2022-01-01     | In Betrieb            | 2022-01-01T00:00:00Z      |

    And the Gemeindeschlüssel for 'Frankfurt' is '06412000'
    And the total solar potential on rooftops in the city is 100.0 MWp
    And the target year is 2030
    And now frozen at '2022-01-01'

    When the solar city is created based on the Marktstammdatenregister

    Then the calculated highchart has a total of 100.04 MWp and contains the following values
      | year | MWp   | number of solar systems |
      | 2018 | 0.04  | 1                       |
      | 2020 | 0.05  | 2                       |
      | 2022 | 0.05  | 2                       |
      | 2023 | 0.76  | 27                      |
      | 2024 | 3.44  | 122                     |
      | 2025 | 8.61  | 307                     |
      | 2026 | 15.06 | 537                     |
      | 2027 | 19.07 | 681                     |
      | 2028 | 20.87 | 745                     |
      | 2029 | 21.22 | 757                     |
      | 2030 | 10.87 | 388                     |
    And the future available rooftop solar potential is 99.90 MWp
    And the average of all yet installed solar systems is 28 kWp


  Scenario:
  Solar installations in planning whose commissioning date is still in the future are considered active,
  as they have already been installed.
  So active solar installations are solar installations
  that are in operation or in planning if the commissioning date is in the future.

    Given is, that for the Gemeindeschlüssel '06412000', the following solar systems are registered in the Marktstammdatenregister
      | Id | Ort       | Plz   | Nettonennleistung | Inbetriebnahme | BetriebsStatus | DatumLetzteAktualisierung |
      | 1  | Frankfurt | 60314 | 25.0              | 2024-12-01     | In Betrieb     | 2024-12-14T00:00:00Z      |
      | 2  | Frankfurt | 60314 | 25.0              | 2024-12-01     | In Betrieb     | 2024-12-14T00:00:00Z      |
      | 4  | Frankfurt | 60528 | 25.0              | 2024-12-01     | In Betrieb     | 2024-12-14T00:00:00Z      |
      | 5  | Frankfurt | 60528 | 25.0              | 2024-12-15     | In Planung     | 2024-12-14T00:00:00Z      |

    And the Gemeindeschlüssel for 'Frankfurt' is '06412000'
    And the total solar potential on rooftops in the city is 1.0 MWp
    And the target year is 2030
    And now frozen at '2024-12-14'

    When the solar city is created based on the Marktstammdatenregister

    Then the calculated highchart has a total of 1.000 MWp and contains the following values
      | year | MWp   | number of solar systems |
      | 2024 | 0.100 | 4                       |
      | 2025 | 0.100 | 4                       |
      | 2026 | 0.120 | 4                       |
      | 2027 | 0.160 | 6                       |
      | 2028 | 0.180 | 7                       |
      | 2029 | 0.190 | 7                       |
      | 2030 | 0.150 | 5                       |
    And the future available rooftop solar potential is 0.900 MWp


  Scenario:
  Since Balkonkraftwerke are also registered in the Marktstammdatenregister,
  they are to be taken into account and visualized in the highchart
  in order to observe the additions here as well.

  However, they should not be taken into account for the calculation of future annual additions,
  since only the solar potential on roofs is given.
  So neither for the average calculation for the necessary number of solar installations per year,
  nor for the solar potential on roofs available in the future.

    Given is, that the Gemeindeschlüssel for 'Frankfurt' is '06412000'
    And that in 2022 in 'Frankfurt' with Gemeindeschlüssel '06412000', 1000 Balkonkraftwerke (with 0.6 kWp)
    And 400 solar systems on apartment buildings (with 25.0 kWp)
    And 100 on schools (with 100.0 kWp) are registered in the Marktstammdatenregister
    And the total solar potential on rooftops in the city is 1000.00 MWp
    And the target year is 2030
    And now frozen at '2022-01-01'

    When the solar city is created based on the Marktstammdatenregister

    Then the calculated highchart has a total of 1000.60 MWp and contains the following values
      | year | MWp    | number of solar systems |
      | 2022 | 20.60  | 1500                    |
      | 2023 | 26.39  | 659                     |
      | 2024 | 48.37  | 1209                    |
      | 2025 | 90.75  | 2268                    |
      | 2026 | 143.59 | 3589                    |
      | 2027 | 176.47 | 4411                    |
      | 2028 | 191.18 | 4779                    |
      | 2029 | 194.04 | 4851                    |
      | 2030 | 109.21 | 2730                    |
    And the future available rooftop solar potential is 980.00 MWp
    And the average of all yet installed solar systems is 40 kWp
