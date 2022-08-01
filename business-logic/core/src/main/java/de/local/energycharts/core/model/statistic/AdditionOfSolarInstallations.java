package de.local.energycharts.core.model.statistic;

import static java.math.RoundingMode.HALF_DOWN;

import java.math.BigDecimal;
import java.time.Month;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionOfSolarInstallations implements Comparable<AdditionOfSolarInstallations> {

  private Integer year; // required
  private Month month;  // optional
  private Integer numberOfSolarSystems;
  private Long numberOfSolarSystemsUpTo10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;
  private Double totalInstalledMWp;
  private boolean calculatedInFuture;

  public String getYearAndMonth (){
    return year + month.toString();
  }

  public AdditionOfSolarInstallations add(AdditionOfSolarInstallations other) {
    numberOfSolarSystems += other.numberOfSolarSystems;
    numberOfSolarSystemsUpTo10kWp += other.numberOfSolarSystemsUpTo10kWp;
    numberOfSolarSystems10To40kWp += other.numberOfSolarSystems10To40kWp;
    numberOfSolarSystemsFrom40kWp += other.numberOfSolarSystemsFrom40kWp;
    totalInstalledMWp += other.totalInstalledMWp;
    return this;
  }

  public Double getTotalInstalledMWp() {
    return BigDecimal.valueOf(totalInstalledMWp)
        .setScale(2, HALF_DOWN)
        .doubleValue();
  }

  public int compareTo(AdditionOfSolarInstallations other) {
    if (month != null && year.equals(other.year)) {
      return month.compareTo(other.month);
    }
    return year.compareTo(other.year);
  }
}
