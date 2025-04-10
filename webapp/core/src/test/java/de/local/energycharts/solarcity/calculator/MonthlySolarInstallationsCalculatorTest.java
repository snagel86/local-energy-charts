package de.local.energycharts.solarcity.calculator;

import de.local.energycharts.solarcity.model.SolarBuilder;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.Time;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static de.local.energycharts.solarcity.calculator.MonthlySolarInstallationsAssert.assertThat;
import static java.time.Month.*;

class MonthlySolarInstallationsCalculatorTest {

  @Test
  void calculate_monthly_solar_installations() {
    Time.freezeNowAt(LocalDate.of(2024, JANUARY, 1));
    var allSolarSystems = new HashSet<SolarSystem>();

    allSolarSystems.addAll(new SolarBuilder()
        .withYear(2023)
        .withMonth(NOVEMBER)
        .addBalkonkraftwerksWith06kWp(417)
        .addHomesWith5kWp(200)
        .addApartmentBuildingsWith25kWp(50)
        .addSchoolsWith100kWp(25).build()
    );
    allSolarSystems.addAll(new SolarBuilder()
        .withYear(2023)
        .withMonth(DECEMBER)
        .addBalkonkraftwerksWith06kWp(833)
        .addHomesWith5kWp(400)
        .addApartmentBuildingsWith25kWp(100)
        .addSchoolsWith100kWp(50).build()
    );
    allSolarSystems.addAll(new SolarBuilder()
        .withYear(2024)
        .withMonth(JANUARY)
        .addBalkonkraftwerksWith06kWp(1250)
        .addHomesWith5kWp(600)
        .addApartmentBuildingsWith25kWp(150)
        .addSchoolsWith100kWp(75).build()
    );

    var result =
        new MonthlySolarInstallationsCalculator(allSolarSystems)
            .calculateMonthlyInstallations();

    assertThat(result.getAddition(2023, NOVEMBER))
        .hasUpTo1kWpSolarSystems(417)
        .has1To10kWpSolarSystems(200)
        .has10To40kWpSolarSystems(50)
        .hasFrom40kWpSolarSystems(25)
        .hasATotalOfSolarSystems(692)
        .andHasTotalInstalledMWp(5.0);

    assertThat(result.getAddition(2023, DECEMBER))
        .hasUpTo1kWpSolarSystems(833)
        .has1To10kWpSolarSystems(400)
        .has10To40kWpSolarSystems(100)
        .hasFrom40kWpSolarSystems(50)
        .hasATotalOfSolarSystems(1383)
        .andHasTotalInstalledMWp(10.0);

    // As this month has not yet been completed, the addition may not yet be present.
    assertThat(result.getAddition(2024, JANUARY)).isNull();
  }
}