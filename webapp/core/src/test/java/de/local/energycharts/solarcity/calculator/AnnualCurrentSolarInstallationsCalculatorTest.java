package de.local.energycharts.solarcity.calculator;

import de.local.energycharts.solarcity.model.SolarBuilder;
import de.local.energycharts.solarcity.model.AnnualSolarInstallations;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnnualCurrentSolarInstallationsCalculatorTest {

  @Test
  void test_categorization_of_solar_systems() {
    var solarSystems = new SolarBuilder()
        .withYear(2022)
        .addBalkonkraftwerksWith06kWp(833)
        .addHomesWith5kWp(400)
        .addApartmentBuildingsWith25kWp(100)
        .addSchoolsWith100kWp(50)
        .build();
    var calculator = new AnnualCurrentSolarInstallationsCalculator(solarSystems);
    var annualAdditions = calculator.calculateAnnualAdditions();

    assertThat(annualAdditions)
        .containsExactly(AnnualSolarInstallations.Addition.builder()
            .year(2022)
            .numberOfSolarSystems(1383)
            .totalInstalledMWp(10.0)
            .numberOfSolarSystemsUpTo1kWp(833L)
            .numberOfSolarSystems1To10kWp(400L)
            .numberOfSolarSystems10To40kWp(100L)
            .numberOfSolarSystemsFrom40kWp(50L)
            .build()
        );
  }
}