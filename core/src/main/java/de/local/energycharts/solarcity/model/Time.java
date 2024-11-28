package de.local.energycharts.solarcity.model;

import org.jmolecules.ddd.annotation.Entity;

import java.time.*;

import static java.lang.String.format;

@Entity
public class Time {

  private static Instant frozenNow = null;

  public static Instant now() {
    if (frozenNow != null) {
      return frozenNow;
    }
    return Instant.now();
  }

  public static void freezeNowAt(Instant time) {
    Time.frozenNow = time;
  }

  public static void freezeNowAt(LocalDate date) {
    Time.frozenNow = Instant.ofEpochSecond(date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC));
  }

  public static void freezeNowAt(Integer year) {
    Time.frozenNow = Instant.parse(format("%d-01-01T00:00:00.00Z", year));
  }

  public static Integer currentYear() {
    return LocalDate.ofInstant(now(), ZoneId.systemDefault()).getYear();
  }
}
