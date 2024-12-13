package de.local.energycharts.solarcity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jmolecules.ddd.annotation.Entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_PLANNING;

@Entity
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolarSystem {

  private String id;
  private LocalDate commissioning;
  private Double installedGrossPowerkWp;
  private Double installedNetPowerkWp;
  private Instant lastChange;
  private Status status;
  private String operatorName;
  private String name;
  private Integer postcode;

  public enum Status {
    IN_OPERATION,
    IN_PLANNING,
    PERMANENTLY_SHUT_DOWN,
    TEMPORARILY_SHUT_DOWN,
    NONE
  }

  public Integer getCommissioningYear() {
    return commissioning.getYear();
  }

  public LocalDate getCommissioningMonth() {
    return LocalDate.of(commissioning.getYear(), commissioning.getMonth(), 1);
  }

  public boolean isCommissionedWithinTheLastTwelveMonth() {
    return isWithinTheLastTwelveMonth() && butNotThisMonth();
  }

  private boolean isWithinTheLastTwelveMonth() {
    var aYearAgo = LocalDate
        .ofInstant(Time.now(), ZoneId.systemDefault())
        .minusYears(1);
    return (commissioning.withDayOfMonth(1).isAfter(aYearAgo.withDayOfMonth(1))
        || commissioning.withDayOfMonth(1).isEqual(aYearAgo.withDayOfMonth(1)));
  }

  private boolean butNotThisMonth() {
    var now = LocalDate.ofInstant(Time.now(), ZoneId.systemDefault());
    return !(commissioning.getYear() == now.getYear() && commissioning.getMonth() == now.getMonth());
  }

  public boolean isActive() {
    return status.equals(IN_OPERATION) ||
        (status.equals(IN_PLANNING) && commissioning.isAfter(Time.currentDate()));
  }

  public boolean isNotActive() {
    return !isActive();
  }
}
