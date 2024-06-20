package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarBuilder;
import de.local.energycharts.solarcity.model.Time;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolarCityOverviewCalculatorTest {

  @Test
  void calculate_solar_city_overview() {
    var solarSystems = new SolarBuilder()
        .withYear(2022)
        .addBalkonkraftwerksWith06kWp(833)   // =  0.5 MWp
        .addHomesWith5kWp(1000)              // =  5.0 MWp
        .addApartmentBuildingsWith25kWp(200) // =  5.0 MWp
        .addSchoolsWith100kWp(100)           // = 10.0 MWp
        .build();
    var updated = Time.now();
    var calculator = new SolarCityOverviewCalculator(1000.0, 2030, solarSystems, updated);

    var overview = SolarCityOverview.builder()
        .installedRooftopMWpInOperation(20.0)
        .usedRoofSolarPotentialPercent(20.0 / 1000.0 * 100)
        .balkonSolarSystemsInOperation(833L)
        .installedBalkonMWpInOperation(0.5)
        .entireSolarPotentialOnRooftopsMWp(1000.0)
        .targetYear(2030)
        .rooftopSolarSystemsInOperation(1300L)
        .averageRoofSolarSystemMWp(0.02)
        .roofSolarSystemsToBeInstalledByTheTargetYear((int) (1300 / 0.02))
        .updated(updated)
        .build();

    assertThat(calculator.calculateSolarCityOverview())
        .isEqualTo(overview);
  }
}