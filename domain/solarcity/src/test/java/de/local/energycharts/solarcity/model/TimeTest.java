package de.local.energycharts.solarcity.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class TimeTest {

  @Test
  void test_freeze_now() throws InterruptedException {
    Instant frozenNow = Instant.now();
    Time.freezeNowAt(frozenNow);
    Thread.sleep(1);

    assertThat(Time.now()).isEqualTo(frozenNow);
  }
}