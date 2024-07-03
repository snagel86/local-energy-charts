package de.local.energycharts.solarcity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.PERMANENTLY_SHUT_DOWN;

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolarSystem  {

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
    PERMANENTLY_SHUT_DOWN,
    NONE
  }

  public Integer getCommissioningYear() {
    return commissioning != null ? commissioning.getYear() : 1970;
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

  public boolean isInOperation() {
    return status.equals(IN_OPERATION);
  }

  public boolean isPermanentlyShutDown() {
    return status.equals(PERMANENTLY_SHUT_DOWN);
  }
}
