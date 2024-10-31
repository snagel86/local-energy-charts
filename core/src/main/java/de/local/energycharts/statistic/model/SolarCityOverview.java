package de.local.energycharts.statistic.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Data
@Builder
public class SolarCityOverview {

  private Long rooftopSolarSystemsInOperation;
  private Double installedRooftopMWpInOperation;
  private Long balkonSolarSystemsInOperation;
  private Double installedBalkonMWpInOperation;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  private Double averageRoofSolarSystemMWp;
  private Integer roofSolarSystemsToBeInstalledByTheTargetYear;
  private Double usedRoofSolarPotentialPercent;
  private Instant updated;

  public Double getInstalledRooftopMWpInOperation() {
    return BigDecimal.valueOf(installedRooftopMWpInOperation)
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

  public Double getInstalledBalkonMWpInOperation() {
    return BigDecimal.valueOf(installedBalkonMWpInOperation)
        .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
  }
}
