package de.local.energycharts.api.v1.statistic.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import lombok.Data;

@Data
public class SolarCityOverviewResponse {

  private Integer totalSolarInstallations;
  private Double totalInstalledMWp;
  private Double totalSolarPotentialMWp;
  private Double usedSolarPotentialPercent;

  private Long numberOfSolarSystemsUpTo10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;

  private Instant updated;

  public Double getTotalInstalledMWp() {
    return BigDecimal.valueOf(totalInstalledMWp)
        .setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  public Double getUsedSolarPotentialPercent() {
    return BigDecimal.valueOf(usedSolarPotentialPercent)
        .setScale(2, RoundingMode.HALF_UP).doubleValue();
  }
}
