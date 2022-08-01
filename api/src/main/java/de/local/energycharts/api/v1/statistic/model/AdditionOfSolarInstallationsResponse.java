package de.local.energycharts.api.v1.statistic.model;

import lombok.Data;

@Data
public class AdditionOfSolarInstallationsResponse {

  private Integer year;
  private Integer numberOfSolarSystems;
  private Long numberOfSolarSystemsUpTo10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;
  private Double totalInstalledMWp;
  private boolean calculatedInFuture;
}
