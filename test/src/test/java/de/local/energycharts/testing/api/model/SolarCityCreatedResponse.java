package de.local.energycharts.testing.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolarCityCreatedResponse {

  private String id;
  private String cityName;
  private Integer totalNumberOfSolarInstallations;
}
