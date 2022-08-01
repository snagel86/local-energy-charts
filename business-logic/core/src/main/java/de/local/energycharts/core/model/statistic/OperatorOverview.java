package de.local.energycharts.core.model.statistic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperatorOverview {

  private String operatorName;
  private Long numberOfSolarSystems;
  private Long numberOfSolarSystemsUpTo10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;
}
