package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarBuilder;
import org.junit.jupiter.api.Test;

import static de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart.SliceType.*;
import static de.local.energycharts.solarcity.model.calculator.SolarBuildingPieChartAssert.assertThat;

class SolarBuildingPieChartCalculatorTest {

  @Test
  void calculate_pie_chart() {
    var solarSystems = new SolarBuilder()
        .addBalkonkraftwerksWith06kWp(833)
        .addHomesWith5kWp(400)
        .addApartmentBuildingsWith25kWp(100)
        .addSchoolsWith100kWp(50)
        .build();

    var pieChart = new SolarBuildingPieChartCalculator(solarSystems).calculatePieChart();
    assertThat(pieChart.getSlice(BALKONKRAFTWERKE))
        .hasInstalledMWp(0.5).hasCount(833).andHasPercentage(5.0);

    assertThat(pieChart.getSlice(HOMES))
        .hasInstalledMWp(2.0).hasCount(400).andHasPercentage(20.0);

    assertThat(pieChart.getSlice(APARTMENT_BUILDINGS_COMMERCIAL_AND_CO))
        .hasInstalledMWp(2.5).hasCount(100).andHasPercentage(25.0);

    assertThat(pieChart.getSlice(SCHOOLS_INDUSTRIAL_BUILDINGS_AND_CO))
        .hasInstalledMWp(5.0).hasCount(50).andHasPercentage(50.0);
  }
}