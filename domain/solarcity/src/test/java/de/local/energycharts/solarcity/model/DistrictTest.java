package de.local.energycharts.solarcity.model;

import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.NONE;
import static org.assertj.core.api.Assertions.assertThat;

import de.local.energycharts.solarcity.model.statistic.AdditionOfSolarInstallations;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DistrictTest {

  @Test
  void count_all_solar_systems() {
    District district = District.builder()
        .solarSystems(Set.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(NONE).build())
        ).build();

    assertThat(district.countAllSolarSystems()).isEqualTo(1);
  }

  @Test
  void calculate_total_installed() {
    District district = District.builder()
        .solarSystems(Set.of(
            SolarSystem.builder()
                .id("1")
                .status(IN_OPERATION)
                .commissioning(LocalDate.of(2000, 1, 1))
                .installedNetPowerkWp(4.2).build(),
            SolarSystem.builder()
                .id("2")
                .status(IN_OPERATION)
                .commissioning(LocalDate.of(2017, 1, 1))
                .installedNetPowerkWp(25.9).build())
        ).build();

    assertThat(district.calculateTotalInstalledMWp()).isEqualTo(0.0301);
  }

  @Test
  void calculate_annual_addition_of_solar_installations() {
    SolarSystem s1 = SolarSystem.builder()
        .commissioning(LocalDate.of(2000, 1, 1))
        .installedNetPowerkWp(9.7).build();
    SolarSystem s2 = SolarSystem.builder()
        .commissioning(LocalDate.of(2000, 1, 1))
        .installedNetPowerkWp(29.4).build();
    SolarSystem s3 = SolarSystem.builder()
        .commissioning(LocalDate.of(2002, 1, 1))
        .installedNetPowerkWp(110.9).build();
    District district = District.builder()
        .postcode(60314)
        .solarSystems(Set.of(s1, s2, s3)).build();

    AdditionOfSolarInstallations addition1 = AdditionOfSolarInstallations.builder()
        .year(2000)
        .numberOfSolarSystems(2)
        .numberOfSolarSystemsUpTo1kWp(0L)
        .numberOfSolarSystems1To10kWp(1L)
        .numberOfSolarSystems10To40kWp(1L)
        .numberOfSolarSystemsFrom40kWp(0L)
        .totalInstalledMWp(0.04).build();
    AdditionOfSolarInstallations addition2 = AdditionOfSolarInstallations.builder()
        .year(2002)
        .numberOfSolarSystems(1)
        .numberOfSolarSystemsUpTo1kWp(0L)
        .numberOfSolarSystems1To10kWp(0L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(1L)
        .totalInstalledMWp(0.11).build();

    assertThat(district.calculateAnnualAdditionOfSolarInstallations()).containsExactly(addition1, addition2);
  }
}
