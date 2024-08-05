package de.local.energycharts.solarcity.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;

class SolarSystemTest {

  @Test
  void test_is_commissioned_within_twelve_year() {
    var solarSystem = SolarSystem.builder().build();
    Time.freezeNowAt(LocalDate.of(2023, FEBRUARY, 7));

    solarSystem.setCommissioning(LocalDate.of(2022, JANUARY, 31));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isFalse();

    solarSystem.setCommissioning(LocalDate.of(2022, FEBRUARY, 1));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isTrue();

    solarSystem.setCommissioning(LocalDate.of(2023, JANUARY, 1));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isTrue();

    solarSystem.setCommissioning(LocalDate.of(2023, FEBRUARY, 5));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isFalse();
  }
}