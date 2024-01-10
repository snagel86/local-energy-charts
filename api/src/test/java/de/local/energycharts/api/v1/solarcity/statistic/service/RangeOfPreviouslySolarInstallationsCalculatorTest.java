package de.local.energycharts.api.v1.solarcity.statistic.service;

import de.local.energycharts.solarcity.model.Time;
import org.junit.jupiter.api.Test;

import static de.local.energycharts.api.v1.solarcity.statistic.service.RangeOfPreviouslySolarInstallationsCalculator.calculateRange;
import static de.local.energycharts.solarcity.model.Time.currentYear;
import static org.assertj.core.api.Assertions.assertThat;

class RangeOfPreviouslySolarInstallationsCalculatorTest {

  @Test
  void calculate_20_years_when_there_is_no_target_year() {
    assertThat(calculateRange(false, null).inYears())
        .isEqualTo(20);

    assertThat(calculateRange(true, null).inYears())
        .isEqualTo(20);
  }

  @Test
  void always_keep_the_same_time_span_when_toggling() {
    Time.freezeNowAt(2022);
    assertThat(calculateRange(false, 2035).inYears())
        .isEqualTo(10);

    assertThat(calculateRange(true, 2035).inYears())
        .isEqualTo(2035 - currentYear() + 10);
  }
}