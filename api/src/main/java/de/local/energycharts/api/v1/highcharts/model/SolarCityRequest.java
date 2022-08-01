package de.local.energycharts.api.v1.highcharts.model;

import java.util.Set;
import lombok.Data;

@Data
public class SolarCityRequest {

  private String cityName;
  private Set<Integer> postcodes;
  private Double totalSolarPotentialMWp;
  private Integer totalSolarPotentialTargetYear;
}
