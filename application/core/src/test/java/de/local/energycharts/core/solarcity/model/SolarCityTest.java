package de.local.energycharts.core.solarcity.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.lang.Math.round;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThat;

class SolarCityTest {

  @Test
  void was_updated_within() {
    Time.freezeNowAt(Instant.parse("2022-01-01T00:00:00.00Z"));
    var solarCity = new SolarCity();
    solarCity.setUpdated(Time.now().minus(23, HOURS));

    assertThat(solarCity.wasUpdatedWithin(24, HOURS)).isTrue();
    assertThat(solarCity.wasUpdatedWithin(25, HOURS)).isTrue();
    assertThat(solarCity.wasUpdatedWithin(23, HOURS)).isTrue();
    assertThat(solarCity.wasUpdatedWithin(20, HOURS)).isFalse();
  }

  @Test
  void calculate_annual_addition_of_solar_installations() {
    Time.freezeNowAt(2022);
    var solarSystems = new SolarBuilder()
        .withYear(2022)
        .addBalkonkraftwerksWith06kWp(833)   // =  0.5 MWp
        .addHomesWith5kWp(400)               // =  2.0 MWp
        .addPermanentlyShutDownHomes(200)    // =  1.0 MWp
        .addApartmentBuildingsWith25kWp(100) // =  2.5 MWp
        .addSchoolsWith250kWp(50)            // = 12.5 MWp
        .build();

    var frankfurt = new SolarCity()
        .setSolarSystems(solarSystems)
        .setEntireSolarPotentialOnRooftopsMWp(1000.0)
        .setTargetYear(2030);

    var annualAdditions = frankfurt.calculateAnnualSolarInstallations();

    var addition2022 = AnnualSolarInstallations.Addition.builder()
        .year(2022)
        .totalInstalledMWp(18.5)
        .numberOfSolarSystems(1583)
        .numberOfSolarSystemsUpTo1kWp(833L).numberOfSolarSystems1To10kWp(600L)
        .numberOfSolarSystems10To40kWp(100L).numberOfSolarSystemsFrom40kWp(50L).build();
    var addition2023 = AnnualSolarInstallations.Addition.builder()
        .year(2023)
        .numberOfSolarSystems(1017)
        .totalInstalledMWp(24.43).build();
    var addition2024 = AnnualSolarInstallations.Addition.builder()
        .year(2024)
        .numberOfSolarSystems(1955)
        .totalInstalledMWp(46.94).build();
    var addition2025 = AnnualSolarInstallations.Addition.builder()
        .year(2025)
        .numberOfSolarSystems(3764)
        .totalInstalledMWp(90.35).build();
    var addition2026 = AnnualSolarInstallations.Addition.builder()
        .year(2026)
        .numberOfSolarSystems(6020)
        .totalInstalledMWp(144.48).build();
    var addition2027 = AnnualSolarInstallations.Addition.builder()
        .year(2027)
        .numberOfSolarSystems(7423)
        .totalInstalledMWp(178.15).build();
    var addition2028 = AnnualSolarInstallations.Addition.builder()
        .year(2028)
        .numberOfSolarSystems(8051)
        .totalInstalledMWp(193.22).build();
    var addition2029 = AnnualSolarInstallations.Addition.builder()
        .year(2029)
        .numberOfSolarSystems(8173)
        .totalInstalledMWp(196.16).build();
    var addition2030 = AnnualSolarInstallations.Addition.builder()
        .year(2030)
        .numberOfSolarSystems(4552)
        .totalInstalledMWp(109.26).build();

    assertThat(annualAdditions.getAdditions()).containsExactly(
        addition2022,
        addition2023, addition2024, addition2025, addition2026,
        addition2027, addition2028, addition2029, addition2030
    );

    var alreadyDoneOnRooftopsMWp = 2.0 + 1.0 + 2.5 + 12.5;
    var permanentlyShutDownMWp = 1.0;
    var futureMWp = annualAdditions.getAdditions().stream()
        .filter(addition -> addition.getYear() > 2022)
        .mapToDouble(AnnualSolarInstallations.Addition::getTotalInstalledMWp)
        .sum();

    assertThat(round(
        futureMWp
    )).isEqualTo(round(
        frankfurt.getEntireSolarPotentialOnRooftopsMWp()
            - alreadyDoneOnRooftopsMWp
            + permanentlyShutDownMWp
    ));
  }

  @Test
  void add_all_solar_systems() {
    var solarSystems = new SolarBuilder()
        .withYear(2025)
        .addApartmentBuildingsWith25kWp(100)
        .addSchoolsWith250kWp(50)
        .build();

    var frankfurt = SolarCity.createNewSolarCity("Frankfurt");
    frankfurt.addSolarSystems(solarSystems);
    assertThat(frankfurt.getSolarSystems()).hasSize(150);
    assertThat(frankfurt.calculateSolarCityOverview().getInstalledRooftopMWp()).isEqualTo(15.0);


    solarSystems = new SolarBuilder()  // new instance to reset id assignment
        .withYear(2025)
        .addApartmentBuildingsWith25kWp(40)
        .addSchoolsWith250kWp(10)
        .build();

    frankfurt.addSolarSystems(solarSystems);
    // size must be the same, because the id will be overwritten/updated.
    assertThat(frankfurt.getSolarSystems()).hasSize(150);
    // installed capacity on the rooftops must be different because the setup of solar systems has changed.
    assertThat(frankfurt.calculateSolarCityOverview().getInstalledRooftopMWp()).isEqualTo(17.25);
  }
}