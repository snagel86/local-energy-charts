package de.local.energycharts.core.model;

import static de.local.energycharts.core.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.core.model.SolarSystem.Status.NONE;
import static java.time.Month.APRIL;
import static java.time.Month.JANUARY;
import static java.time.Month.SEPTEMBER;
import static org.assertj.core.api.Assertions.assertThat;

import de.local.energycharts.core.model.statistic.AdditionOfSolarInstallations;
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
        .numberOfSolarSystemsUpTo10kWp(1L)
        .numberOfSolarSystems10To40kWp(1L)
        .numberOfSolarSystemsFrom40kWp(0L)
        .totalInstalledMWp(0.04).build();
    AdditionOfSolarInstallations addition2 = AdditionOfSolarInstallations.builder()
        .year(2002)
        .numberOfSolarSystems(1)
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(1L)
        .totalInstalledMWp(0.11).build();

    assertThat(district.calculateAnnualAdditionOfSolarInstallations()).containsExactly(addition1, addition2);
  }

  @Test
  void calculate_monthly_addition_of_solar_installations() {
    SolarSystem s1 = SolarSystem.builder()
        .commissioning(LocalDate.of(2019, 9, 1))
        .installedNetPowerkWp(39.9).build();
    SolarSystem s2 = SolarSystem.builder()
        .commissioning(LocalDate.of(2020, 1, 1))
        .installedNetPowerkWp(9.9).build();
    SolarSystem s3 = SolarSystem.builder()
        .commissioning(LocalDate.of(2020, 4, 1))
        .installedNetPowerkWp(110.2).build();
    District district = District.builder()
        .postcode(60314)
        .solarSystems(Set.of(s1, s2, s3)).build();

    AdditionOfSolarInstallations september2019 = AdditionOfSolarInstallations.builder()
        .year(2019)
        .month(SEPTEMBER)
        .numberOfSolarSystems(1)
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(1L)
        .numberOfSolarSystemsFrom40kWp(0L)
        .totalInstalledMWp(0.04).build();
    AdditionOfSolarInstallations january2020 = AdditionOfSolarInstallations.builder()
        .year(2020)
        .month(JANUARY)
        .numberOfSolarSystems(1)
        .numberOfSolarSystemsUpTo10kWp(1L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(0L)
        .totalInstalledMWp(0.01).build();
    AdditionOfSolarInstallations april2020 = AdditionOfSolarInstallations.builder()
        .year(2020)
        .month(APRIL)
        .numberOfSolarSystems(1)
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(1L)
        .totalInstalledMWp(0.11).build();

    assertThat(district.calculateMonthlyAdditionOfSolarInstallations())
        .containsExactlyInAnyOrder(september2019, january2020, april2020);
  }
}
