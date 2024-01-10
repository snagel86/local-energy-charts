package de.local.energycharts.api.v1.solarcity.statistic.model.highcharts;

import lombok.Data;

@Data
public class SolarCityRequest {

  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
}
