package de.local.energycharts.core.solarcity.model;

import de.local.energycharts.core.solarcity.model.SolarSystem;
import de.local.energycharts.core.solarcity.model.Time;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static de.local.energycharts.core.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.core.solarcity.model.SolarSystem.Status.IN_PLANNING;
import static java.time.Month.*;
import static org.assertj.core.api.Assertions.assertThat;

class SolarSystemTest {

  @Test
  void test_is_commissioned_within_twelve_year() {
    Time.freezeNowAt(LocalDate.of(2023, FEBRUARY, 7));
    var solarSystem = SolarSystem.builder().build();

    solarSystem.setCommissioning(LocalDate.of(2022, JANUARY, 31));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isFalse();

    solarSystem.setCommissioning(LocalDate.of(2022, FEBRUARY, 1));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isTrue();

    solarSystem.setCommissioning(LocalDate.of(2023, JANUARY, 1));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isTrue();

    solarSystem.setCommissioning(LocalDate.of(2023, FEBRUARY, 5));
    assertThat(solarSystem.isCommissionedWithinTheLastTwelveMonth()).isFalse();
  }

  @Test
  void test_is_active() {
    Time.freezeNowAt(LocalDate.of(2024, DECEMBER, 14));
    var solarSystem = SolarSystem.builder().build();

    solarSystem.setStatus(IN_OPERATION);
    assertThat(solarSystem.isActive()).isTrue();

    solarSystem.setStatus(IN_PLANNING);
    solarSystem.setCommissioning(LocalDate.of(2024, DECEMBER, 1));
    assertThat(solarSystem.isActive()).isFalse();

    solarSystem.setStatus(IN_PLANNING);
    solarSystem.setCommissioning(LocalDate.of(2024, DECEMBER, 15));
    assertThat(solarSystem.isActive()).isTrue();
  }
}