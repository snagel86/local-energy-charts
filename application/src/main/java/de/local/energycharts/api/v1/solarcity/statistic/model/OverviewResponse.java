package de.local.energycharts.api.v1.solarcity.statistic.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import lombok.Data;

@Data
public class OverviewResponse {

  private Long rooftopSolarSystems;
  private Double installedRooftopMWp;
  private Long balkonSolarSystems;
  private Double installedBalkonMWp;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  private Double averageRoofSolarSystemMWp;
  private Integer roofSolarSystemsToBeInstalledByTheTargetYear;
  private Double usedRoofSolarPotentialPercent;
  private Instant updated;

  public Double getInstalledRooftopMWp() {
    return BigDecimal.valueOf(installedRooftopMWp)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }

  public Double getUsedRoofSolarPotentialPercent() {
    return BigDecimal.valueOf(usedRoofSolarPotentialPercent)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }

  public Double getAverageRoofSolarSystemMWp() {
    return BigDecimal.valueOf(averageRoofSolarSystemMWp)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }

  public Double getInstalledBalkonMWp() {
    return BigDecimal.valueOf(installedBalkonMWp)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }
}
