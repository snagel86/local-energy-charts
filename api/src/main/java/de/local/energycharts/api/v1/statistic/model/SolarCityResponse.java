package de.local.energycharts.api.v1.statistic.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolarCityResponse {

  private String cityName;
  private List<AdditionOfSolarInstallationsResponse> annualAdditionsOfSolarInstallations;
}
