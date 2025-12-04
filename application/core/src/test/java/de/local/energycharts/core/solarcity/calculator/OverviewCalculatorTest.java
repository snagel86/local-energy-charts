package de.local.energycharts.core.solarcity.calculator;

import de.local.energycharts.core.solarcity.model.SolarCity;
import de.local.energycharts.core.solarcity.model.Overview;
import de.local.energycharts.core.solarcity.model.Time;
import de.local.energycharts.core.solarcity.model.SolarBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OverviewCalculatorTest {

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
    var calculator = new OverviewCalculator(
        new SolarCity()
            .setEntireSolarPotentialOnRooftopsMWp(1000.0)
            .setTargetYear(2030)
            .setSolarSystems(solarSystems)
            .setUpdated(updated)
    );

    var overview = Overview.builder()
        .installedRooftopMWp(20.0)
        .usedRoofSolarPotentialPercent(20.0 / 1000.0 * 100)
        .balkonSolarSystems(833L)
        .installedBalkonMWp(0.5)
        .entireSolarPotentialOnRooftopsMWp(1000.0)
        .targetYear(2030)
        .rooftopSolarSystems(1300L)
        .averageRoofSolarSystemMWp(0.02)
        .roofSolarSystemsToBeInstalledByTheTargetYear((int) (1300 / 0.02))
        .updated(updated)
        .build();

    assertThat(calculator.calculateSolarCityOverview())
        .isEqualTo(overview);
  }
}