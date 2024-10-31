package de.local.energycharts.solarcity.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThat;

class SolarCityTest {

  @Test
  void was_updated_within() {
    Time.freezeNowAt(Instant.parse("2022-01-01T00:00:00.00Z"));
    var solarCity = new SolarCity();
    solarCity.setUpdated(Time.now().minus(23, HOURS));

    assertThat(solarCity.wasUpdatedWithin(24, HOURS)).isTrue();
    assertThat(solarCity.wasUpdatedWithin(25, HOURS)).isTrue();
    assertThat(solarCity.wasUpdatedWithin(23, HOURS)).isTrue();
    assertThat(solarCity.wasUpdatedWithin(20, HOURS)).isFalse();
  }
}