package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import org.assertj.core.api.AbstractAssert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class MonthlySolarInstallationsAssert extends AbstractAssert<MonthlySolarInstallationsAssert, MonthlySolarInstallations.Addition> {

  public MonthlySolarInstallationsAssert(MonthlySolarInstallations.Addition actual) {
    super(actual, MonthlySolarInstallationsAssert.class);
  }

  public static MonthlySolarInstallationsAssert assertThat(MonthlySolarInstallations.Addition actual) {
    return new MonthlySolarInstallationsAssert(actual);
  }

  public MonthlySolarInstallationsAssert andHasTotalInstalledMWp(Double expectedInstalledMWp) {
    isNotNull();

    // check condition
    if (!Objects.equals(round(actual.getTotalInstalledMWp()), round(expectedInstalledMWp))) {
      failWithMessage("Expected installedMWp to be <%s> but was <%s>", expectedInstalledMWp, round(actual.getTotalInstalledMWp()));
    }
    return this;
  }

  public MonthlySolarInstallationsAssert hasATotalOfSolarSystems(Integer expectedNumber) {
    isNotNull();

    // check condition
    if (!Objects.equals(actual.getNumberOfSolarSystems(), expectedNumber)) {
      failWithMessage("Expected number to be <%s> but was <%s>", expectedNumber, actual.getNumberOfSolarSystems());
    }
    return this;
  }

  public MonthlySolarInstallationsAssert hasUpTo1kWpSolarSystems(Integer expectedNumber) {
    isNotNull();

    // check condition
    if (!Objects.equals(actual.getNumberOfSolarSystemsUpTo1kWp().intValue(), expectedNumber)) {
      failWithMessage("Expected number to be <%s> but was <%s>", expectedNumber, actual.getNumberOfSolarSystemsUpTo1kWp());
    }
    return this;
  }

  public MonthlySolarInstallationsAssert has1To10kWpSolarSystems(Integer expectedNumber) {
    isNotNull();

    // check condition
    if (!Objects.equals(actual.getNumberOfSolarSystems1To10kWp().intValue(), expectedNumber)) {
      failWithMessage("Expected number to be <%s> but was <%s>", expectedNumber, actual.getNumberOfSolarSystems1To10kWp());
    }
    return this;
  }

  public MonthlySolarInstallationsAssert has10To40kWpSolarSystems(Integer expectedNumber) {
    isNotNull();

    // check condition
    if (!Objects.equals(actual.getNumberOfSolarSystems10To40kWp().intValue(), expectedNumber)) {
      failWithMessage("Expected number to be <%s> but was <%s>", expectedNumber, actual.getNumberOfSolarSystems10To40kWp());
    }
    return this;
  }

  public MonthlySolarInstallationsAssert hasFrom40kWpSolarSystems(Integer expectedNumber) {
    isNotNull();

    // check condition
    if (!Objects.equals(actual.getNumberOfSolarSystemsFrom40kWp().intValue(), expectedNumber)) {
      failWithMessage("Expected number to be <%s> but was <%s>", expectedNumber, actual.getNumberOfSolarSystemsFrom40kWp());
    }
    return this;
  }

//  public MonthlySolarInstallationsAssert isNull() {
//
//    // check condition
//    if (actual != null) {
//      failWithMessage("Expected number to be <%s> but was <%s>", expectedNumber, actual.getNumberOfSolarSystemsFrom40kWp());
//    }
//    return this;
//  }

  private BigDecimal round(double number) {
    return new BigDecimal(number).setScale(2, RoundingMode.HALF_UP);
  }
}
