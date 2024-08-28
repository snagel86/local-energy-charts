package de.local.energycharts.api.v1.solarcity.statistic.model.highcharts;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolarCityRequestTest {

  @Test
  void eo_solar_roof_potential_in_MWp() {
    var request = new SolarCityRequest();
    request.setEoSolarRoofPotential("700,4 mWh");
    assertThat(request.getSolarRoofPotentialMWp()).isEqualTo(0.7004); // 700 MWh -> 0.700 MWp

    request.setEoSolarRoofPotential("9 Gwh");
    assertThat(request.getSolarRoofPotentialMWp()).isEqualTo(9); // 2 GWh -> 2 MWp

    request.setEoSolarRoofPotential("2.1 TWh");
    assertThat(request.getSolarRoofPotentialMWp()).isEqualTo(2_100.0); // 2.1 TWh -> 2,100.0 MWp
  }
}