package de.local.energycharts.api.v1.solarcity.statistic.model.highcharts;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolarCityRequestTest {

  @Test
  void eo_solar_roof_potential_in_MWp() {
    var request = new SolarCityRequest();
    request.setEoSolarRoofPotential("700 mWh");
    assertThat(request.getSolarRoofPotentialMWp()).isEqualTo(0.7); // 700 MWh -> 0.7 MWp

    request.setEoSolarRoofPotential("21,42 Gwh");
    assertThat(request.getSolarRoofPotentialMWp()).isEqualTo(21.42); // 21.42 GWh -> 21.42 MWp

    request.setEoSolarRoofPotential("2.1 TWh");
    assertThat(request.getSolarRoofPotentialMWp()).isEqualTo(2_100.0); // 2.1 TWh -> 2,100.0 MWp
  }
}