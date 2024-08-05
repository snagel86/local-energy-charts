package de.local.energycharts.solarcity.model.statistic;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

import static de.local.energycharts.solarcity.model.Time.currentYear;
import static java.math.RoundingMode.HALF_DOWN;

@Data
@Accessors(chain = true)
@Builder
public class AdditionOfSolarInstallations implements Comparable<AdditionOfSolarInstallations> {

  private String cityName;
  private Integer year;
  private Integer numberOfSolarSystems;
  private Long numberOfSolarSystemsUpTo1kWp;
  private Long numberOfSolarSystems1To10kWp;
  private Long numberOfSolarSystems10To40kWp;
  private Long numberOfSolarSystemsFrom40kWp;
  private Double totalInstalledMWp;

  public Double getTotalInstalledMWp() {
    return BigDecimal.valueOf(totalInstalledMWp)
        .setScale(2, HALF_DOWN)
        .doubleValue();
  }

  public boolean isCurrent(){
    return year.equals(currentYear());
  }

  public boolean isFuture(){
    return year > currentYear();
  }
  public int compareTo(AdditionOfSolarInstallations other) {
    return year.compareTo(other.year);
  }
}
