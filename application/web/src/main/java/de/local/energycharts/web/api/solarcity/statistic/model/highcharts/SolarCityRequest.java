package de.local.energycharts.web.api.solarcity.statistic.model.highcharts;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class SolarCityRequest {

  private String eoSolarRoofPotential;
  private Integer targetYear;
  private Integer years;

  public Double getSolarRoofPotentialMWp() {
    var eoSolarRoofPotentialDouble = getEoSolarRoofPotentialAsDouble();
    if (eoSolarRoofPotentialDouble != null) {
      if (eoSolarRoofPotential.toLowerCase().contains("twh")) {
        return eoSolarRoofPotentialDouble * 1000.0;
      }

      if (eoSolarRoofPotential.toLowerCase().contains("gwh")) {
        return eoSolarRoofPotentialDouble;
      }

      if (eoSolarRoofPotential.toLowerCase().contains("mwh")) {
        return eoSolarRoofPotentialDouble / 1000.0;
      }
    }
    return null;
  }

  private Double getEoSolarRoofPotentialAsDouble() {
    if (eoSolarRoofPotential != null) {
      StringBuilder doubleNumber = new StringBuilder();
      Matcher matcher = Pattern
          .compile("(\\d+[.]\\d+)|(\\d+)")
          .matcher(eoSolarRoofPotential.replace(",", "."));

      while (matcher.find()) {
        doubleNumber.append(matcher.group());
      }

      try {
        return Double.parseDouble(doubleNumber.toString());
      } catch (NumberFormatException ignored) {
      }
    }

    return null;
  }
}
