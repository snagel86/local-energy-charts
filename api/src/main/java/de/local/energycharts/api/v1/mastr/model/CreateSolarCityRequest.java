package de.local.energycharts.api.v1.mastr.model;

import java.util.Set;
import lombok.Data;

@Data
public class CreateSolarCityRequest {

  private String cityName;
  private Set<Integer> postcodes;
  private Double totalSolarPotentialMWp;
  private Integer totalSolarPotentialTargetYear;
}
