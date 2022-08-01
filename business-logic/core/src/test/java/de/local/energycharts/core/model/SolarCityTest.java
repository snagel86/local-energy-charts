package de.local.energycharts.core.model;

import static de.local.energycharts.core.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.core.model.SolarSystem.Status.NONE;
import static de.local.energycharts.core.model.SolarSystem.Status.PERMANENTLY_SHUT_DOWN;
import static org.assertj.core.api.Assertions.assertThat;

import de.local.energycharts.core.model.statistic.AdditionOfSolarInstallations;
import de.local.energycharts.core.model.statistic.OperatorOverview;
import de.local.energycharts.core.model.statistic.SolarCityOverview;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolarCityTest {

  private SolarCity frankfurt;

  @BeforeEach
  void createSolarCityFrankfurt() {
    SolarSystem s1 = SolarSystem.builder()
        .operatorName("Mainova AG")
        .commissioning(LocalDate.of(2000, 1, 1))
        .installedNetPowerkWp(9.8)
        .status(NONE).build();
    SolarSystem s2 = SolarSystem.builder()
        .operatorName("Mainova AG")
        .commissioning(LocalDate.of(2000, 1, 1))
        .installedNetPowerkWp(40.4)
        .status(IN_OPERATION).build();
    SolarSystem s3 = SolarSystem.builder()
        .operatorName("Sonneninitiative e.V.")
        .commissioning(LocalDate.of(2002, 1, 1))
        .installedNetPowerkWp(250.1)
        .status(IN_OPERATION).build();
    District ostend = District.builder()
        .postcode(60314)
        .solarSystems(Set.of(s1, s2, s3)).build();

    SolarSystem s4 = SolarSystem.builder()
        .operatorName("Mainova AG")
        .commissioning(LocalDate.of(2000, 1, 1))
        .installedNetPowerkWp(20.1)
        .status(IN_OPERATION).build();
    SolarSystem s5 = SolarSystem.builder()
        .operatorName("Sonneninitiative e.V.")
        .commissioning(LocalDate.of(2017, 1, 1))
        .installedNetPowerkWp(100.9)
        .status(IN_OPERATION).build();
    District sachsenhausen = District.builder()
        .postcode(60528)
        .solarSystems(Set.of(s4, s5)).build();

    frankfurt = new SolarCity(2022);
    frankfurt
        .setDistricts(Set.of(ostend, sachsenhausen))
        .setTotalSolarPotentialMWp(17.0)
        .setTargetYear(2030);
  }

  @Test
  void calculate_number_of_solar_installations() {
    assertThat(frankfurt.calculateTotalNumberOfSolarInstallations())
        .isEqualTo(4);
  }

  @Test
  void calculate_solar_city_overview() {
    var totalInstalledKWpInOperation = frankfurt.getDistricts().stream()
        .map(District::getSolarSystems).flatMap(Collection::stream)
        .filter(SolarSystem::isInOperation)
        .mapToDouble(SolarSystem::getInstalledNetPowerkWp)
        .sum();

    var overview = SolarCityOverview.builder()
        .totalSolarInstallations(4)
        .totalInstalledMWp(0.41)
        .totalSolarPotentialMWp(17.0)
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(1L)
        .numberOfSolarSystemsFrom40kWp(3L)
        .usedSolarPotentialPercent(totalInstalledKWpInOperation / 17000.0 * 100)
        .build();

    assertThat(frankfurt.calculateSolarCityOverview())
        .isEqualTo(overview);
  }

  @Test
  void calculate_annual_addition_of_solar_installations() {
    frankfurt.setTotalSolarPotentialMWp(null);

    AdditionOfSolarInstallations addition1 = AdditionOfSolarInstallations.builder()
        .year(2000)
        .totalInstalledMWp(0.07)
        .numberOfSolarSystems(3) // 2 in ostend, 1 in sachsenhausen
        .numberOfSolarSystemsUpTo10kWp(1L)
        .numberOfSolarSystems10To40kWp(1L)
        .numberOfSolarSystemsFrom40kWp(1L).build();
    AdditionOfSolarInstallations addition2 = AdditionOfSolarInstallations.builder()
        .year(2002)
        .totalInstalledMWp(0.25)
        .numberOfSolarSystems(1) // 1 in ostend
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(1L).build();
    AdditionOfSolarInstallations addition3 = AdditionOfSolarInstallations.builder()
        .year(2017)
        .totalInstalledMWp(0.10)
        .numberOfSolarSystems(1) // 1 in sachsenhausen
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(1L).build();

    assertThat(frankfurt.calculateAnnualAdditionOfSolarInstallations())
        .containsExactly(addition1, addition2, addition3);
  }

  @Test
  void calculate_annual_addition_of_solar_installations_with_future_additions() {
    AdditionOfSolarInstallations addition2000 = AdditionOfSolarInstallations.builder()
        .numberOfSolarSystems(3) // 2 in ostend, 1 in sachsenhausen
        .year(2000)
        .totalInstalledMWp(0.07)
        .numberOfSolarSystemsUpTo10kWp(1L)
        .numberOfSolarSystems10To40kWp(1L)
        .numberOfSolarSystemsFrom40kWp(1L)
        .build();
    AdditionOfSolarInstallations addition2002 = AdditionOfSolarInstallations.builder()
        .numberOfSolarSystems(1) // 1 in ostend
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(1L)
        .year(2002)
        .totalInstalledMWp(0.25).build();
    AdditionOfSolarInstallations addition2017 = AdditionOfSolarInstallations.builder()
        .numberOfSolarSystems(1) // 1 in sachsenhausen
        .numberOfSolarSystemsUpTo10kWp(0L)
        .numberOfSolarSystems10To40kWp(0L)
        .numberOfSolarSystemsFrom40kWp(1L)
        .year(2017)
        .totalInstalledMWp(0.10).build();

    AdditionOfSolarInstallations addition2023 = AdditionOfSolarInstallations.builder()
        .year(2023)
        .totalInstalledMWp(0.21)
        .calculatedInFuture(true).build();
    AdditionOfSolarInstallations addition2024 = AdditionOfSolarInstallations.builder()
        .year(2024)
        .totalInstalledMWp(0.64)
        .calculatedInFuture(true).build();
    AdditionOfSolarInstallations addition2025 = AdditionOfSolarInstallations.builder()
        .year(2025)
        .totalInstalledMWp(1.46)
        .calculatedInFuture(true).build();
    AdditionOfSolarInstallations addition2026 = AdditionOfSolarInstallations.builder()
        .year(2026)
        .totalInstalledMWp(2.48)
        .calculatedInFuture(true).build();
    AdditionOfSolarInstallations addition2027 = AdditionOfSolarInstallations.builder()
        .year(2027)
        .totalInstalledMWp(3.12)
        .calculatedInFuture(true).build();
    AdditionOfSolarInstallations addition2028 = AdditionOfSolarInstallations.builder()
        .year(2028)
        .totalInstalledMWp(3.40)
        .calculatedInFuture(true).build();
    AdditionOfSolarInstallations addition2029 = AdditionOfSolarInstallations.builder()
        .year(2029)
        .totalInstalledMWp(3.46)
        .calculatedInFuture(true).build();
    AdditionOfSolarInstallations addition2030 = AdditionOfSolarInstallations.builder()
        .year(2030)
        .totalInstalledMWp(1.82)
        .calculatedInFuture(true).build();

    assertThat(frankfurt.calculateAnnualAdditionOfSolarInstallations())
        .containsExactly(
            addition2000, addition2002, addition2017,
            addition2023, addition2024, addition2025,
            addition2026, addition2027, addition2028,
            addition2029, addition2030
        );

    double alreadyDoneMWp = Stream.of(
        addition2000, addition2002, addition2017
    ).mapToDouble(AdditionOfSolarInstallations::getTotalInstalledMWp).sum();
    double futureMWp = Stream.of(
        addition2023, addition2024, addition2025, addition2026,
        addition2027, addition2028, addition2029, addition2030
    ).mapToDouble(AdditionOfSolarInstallations::getTotalInstalledMWp).sum();
    double inactiveSolarSystem1From2000 = 0.01; // kWp

    assertThat(
        frankfurt.getTotalSolarPotentialMWp() - alreadyDoneMWp + inactiveSolarSystem1From2000)
        .isEqualTo(futureMWp);
  }

  @Test
  void calculate_total_installed_gross_power_MWp() {
    SolarSystem s1 = SolarSystem.builder()
        .commissioning(LocalDate.of(2020, 1, 1))
        .installedNetPowerkWp(10.0)
        .status(IN_OPERATION).build();
    SolarSystem s2 = SolarSystem.builder()
        .commissioning(LocalDate.of(2020, 1, 1))
        .installedNetPowerkWp(10.0)
        .status(NONE).build();
    SolarSystem s3 = SolarSystem.builder()
        .commissioning(LocalDate.of(2020, 1, 1))
        .installedNetPowerkWp(10.0)
        .status(PERMANENTLY_SHUT_DOWN).build();
    District sachsenhausen = District.builder()
        .postcode(60528)
        .solarSystems(Set.of(s1, s2, s3)).build();

    SolarCity frankfurt = new SolarCity(2022);
    frankfurt.setDistricts(Set.of(sachsenhausen));

    assertThat(frankfurt.calculateAlreadyInstalledMWp())
        .isEqualTo(0.01); // only count solar systems, still in operation
  }

  @Test
  void create_operator_overview() {
    assertThat(
        frankfurt.calculateOperatorOverview()
    ).isEqualTo(Set.of(
        OperatorOverview.builder()
            .operatorName("Sonneninitiative e.V.")
            .numberOfSolarSystems(2L)
            .numberOfSolarSystemsUpTo10kWp(0L)
            .numberOfSolarSystems10To40kWp(0L)
            .numberOfSolarSystemsFrom40kWp(2L).build(),
        OperatorOverview.builder()
            .operatorName("Mainova AG")
            .numberOfSolarSystems(2L)
            .numberOfSolarSystemsUpTo10kWp(0L)
            .numberOfSolarSystems10To40kWp(1L)
            .numberOfSolarSystemsFrom40kWp(1L).build()
        )
    );
  }
}