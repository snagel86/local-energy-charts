package de.local.energycharts.core.solarcity.calculator;

import de.local.energycharts.core.solarcity.model.SolarBuildingPieChart;
import org.assertj.core.api.AbstractAssert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class SolarBuildingPieChartAssert extends AbstractAssert<SolarBuildingPieChartAssert, SolarBuildingPieChart.Slice> {

  public SolarBuildingPieChartAssert(SolarBuildingPieChart.Slice actual) {
    super(actual, SolarBuildingPieChartAssert.class);
  }

  public static SolarBuildingPieChartAssert assertThat(SolarBuildingPieChart.Slice actual) {
    return new SolarBuildingPieChartAssert(actual);
  }

  public SolarBuildingPieChartAssert hasInstalledMWp(Double expectedInstalledMWp) {
    isNotNull();

    // check condition
    if (!Objects.equals(round(actual.getInstalledMWp()), round(expectedInstalledMWp))) {
      failWithMessage("Expected InstalledMWp to be <%s> but was <%s>", expectedInstalledMWp, round(actual.getInstalledMWp()));
    }
    return this;
  }

  public SolarBuildingPieChartAssert hasCount(Integer expectedCount) {
    isNotNull();

    // check condition
    if (!Objects.equals(actual.getCount(), expectedCount)) {
      failWithMessage("Expected count to be <%s> but was <%s>", expectedCount, actual.getCount());
    }
    return this;
  }

  public void andHasPercentage(Double expectedPercentage) {
    isNotNull();

    // check condition
    if (!Objects.equals(round(actual.getPercentage()), round(expectedPercentage))) {
      failWithMessage("Expected percentage to be <%s> but was <%s>", expectedPercentage, round(actual.getPercentage()));
    }
  }

  private BigDecimal round(double number) {
    return new BigDecimal(number).setScale(2, RoundingMode.HALF_UP);
  }
}
