package de.local.energycharts.core.model.statistic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class FutureAdditionOfSolarInstallationsCalculatorTest {

  @Test
  void get_annual_installed_MWp_from_previous_year() {
    AdditionOfSolarInstallations addition2019 = AdditionOfSolarInstallations.builder()
        .year(2019)
        .totalInstalledMWp(10.0).build();
    AdditionOfSolarInstallations addition2022 = AdditionOfSolarInstallations.builder()
        .year(2022)
        .totalInstalledMWp(10.4).build();

    FutureAdditionOfSolarInstallationsCalculator calculator =
        new FutureAdditionOfSolarInstallationsCalculator(
        null, null, null, null,
            2022
        );

    assertThat(
        calculator.getAnnualInstalledMWpFromPreviousYear(List.of(addition2019, addition2022)))
        .isEqualTo(10.0); // = 2019

    assertThat(calculator.getAnnualInstalledMWpFromPreviousYear(List.of(addition2022)))
        .isEqualTo(0.0); // = default
  }
}