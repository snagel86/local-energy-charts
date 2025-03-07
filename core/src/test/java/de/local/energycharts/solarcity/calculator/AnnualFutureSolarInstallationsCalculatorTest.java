package de.local.energycharts.solarcity.calculator;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.Time;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static de.local.energycharts.solarcity.model.AnnualSolarInstallations.Addition;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.PERMANENTLY_SHUT_DOWN;
import static org.assertj.core.api.Assertions.assertThat;

class AnnualFutureSolarInstallationsCalculatorTest {

  @Test
  void return_empty_list_when_target_year_less_than_3_years_in_future() {
    Time.freezeNowAt(Instant.parse("2024-01-01T00:00:00.00Z"));
    var calculator = new AnnualFutureSolarInstallationsCalculator(
        new SolarCity().setTargetYear(2026),
        null
    );

    assertThat(calculator.calculateAnnualAdditions()).isEmpty();
  }

  @Test
  void get_annual_installed_MWp_from_previous_year() {
    var addition2019 = Addition.builder()
        .year(2019)
        .totalInstalledMWp(10.0).build();
    var addition2022 = Addition.builder()
        .year(2022)
        .totalInstalledMWp(10.4).build();
    var additionsDoneYet = List.of(addition2019, addition2022);
    var calculator = new AnnualFutureSolarInstallationsCalculator(null, additionsDoneYet);
    Time.freezeNowAt(Instant.parse("2022-01-01T00:00:00.00Z"));

    assertThat(calculator.getAnnualInstalledMWpFromPreviousYear())
        .isEqualTo(10.0); // = 2019
  }

  @Test
  void get_annual_installed_MWp_from_current_year() {
    var addition2019 = Addition.builder()
        .year(2019)
        .totalInstalledMWp(10.0).build();
    var addition2022 = Addition.builder()
        .year(2022)
        .totalInstalledMWp(10.4).build();
    var additionsDoneYet = List.of(addition2019, addition2022);
    var calculator = new AnnualFutureSolarInstallationsCalculator(null, additionsDoneYet);
    Time.freezeNowAt(Instant.parse("2022-01-01T00:00:00.00Z"));

    assertThat(calculator.getAnnualInstalledMWpFromCurrentYear())
        .isEqualTo(10.4); // = 2022
  }

  @Test
  void get_highest_last_annual_installed_MWp() {
    var addition2021 = Addition.builder()
        .year(2021)
        .totalInstalledMWp(10.0).build();
    var addition2022 = Addition.builder()
        .year(2022)
        .totalInstalledMWp(5.4).build();
    var additionsDoneYet = List.of(addition2021, addition2022);
    var calculator = new AnnualFutureSolarInstallationsCalculator(null, additionsDoneYet);
    Time.freezeNowAt(Instant.parse("2022-01-01T00:00:00.00Z"));

    assertThat(calculator.getHighestLastAnnualInstalledMWp())
        .isEqualTo(10.0); // 2021

    addition2022.setTotalInstalledMWp(10.1);
    assertThat(calculator.getHighestLastAnnualInstalledMWp())
        .isEqualTo(10.1); // 2022
  }

  @Test
  void calculate_yet_to_be_installed_MWp_on_rooftops() {
    // must not be taken into account because no roof solar.
    var balkonkraftwerk = SolarSystem.builder().installedNetPowerkWp(1.0).status(IN_OPERATION).build();
    // roof solar.
    var school = SolarSystem.builder().installedNetPowerkWp(99.0).status(IN_OPERATION).build();
    var solarSystems = Set.of(balkonkraftwerk, school);
    var calculator = new AnnualFutureSolarInstallationsCalculator(
        new SolarCity()
            .setEntireSolarPotentialOnRooftopsMWp(100.0)
            .setSolarSystems(solarSystems),
        null
    );
    assertThat(calculator.calculateYetToBeInstalledMWp())
        .isEqualTo(99.901); // 100 MW - 99.0 kW = 100 MW - 0.099 MW = 99.901 MW
  }

  @Test
  void calculate_installed_MWp_in_operation() {
    var solarSystem1 = SolarSystem.builder().installedNetPowerkWp(10.0).status(IN_OPERATION).build();
    var solarSystem2 = SolarSystem.builder().installedNetPowerkWp(20.0).status(IN_OPERATION).build();
    var solarSystem3 = SolarSystem.builder().installedNetPowerkWp(5.0).status(IN_OPERATION).build();
    var solarSystem4 = SolarSystem.builder().installedNetPowerkWp(25.0).status(PERMANENTLY_SHUT_DOWN).build();
    var solarSystems = Set.of(solarSystem1, solarSystem2, solarSystem3, solarSystem4);
    var calculator = new AnnualFutureSolarInstallationsCalculator(
        new SolarCity().setSolarSystems(solarSystems),
        null
    );

    assertThat(calculator.calculateInstalledActiveMWp())
        .isEqualTo(0.035);
  }

  @Test
  void calculate_average_MWp_of_all_solar_installations() {
    var solarSystem1 = SolarSystem.builder().installedNetPowerkWp(10.0).build();
    var solarSystem2 = SolarSystem.builder().installedNetPowerkWp(20.0).build();
    var solarSystem3 = SolarSystem.builder().installedNetPowerkWp(5.0).build();
    var solarSystem4 = SolarSystem.builder().installedNetPowerkWp(25.0).build();
    var solarSystems = Set.of(solarSystem1, solarSystem2, solarSystem3, solarSystem4);

    var calculator = new AnnualFutureSolarInstallationsCalculator(
        new SolarCity().setSolarSystems(solarSystems),
        null
    );

    assertThat(calculator.calculateAverageRoofSolarMWp())
        .isEqualTo(0.015);
  }
}