package de.local.energycharts.api.v1.statistic.model;

import lombok.Data;

@Data
public class OperatorOverviewResponse {

  private String operatorName;
  private Long numberOfSolarSystems;
  private Long numberOfSolarSystemsUpTo10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;
}
