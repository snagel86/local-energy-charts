package de.local.energycharts.core.model;

import static de.local.energycharts.core.model.SolarSystem.Status.IN_OPERATION;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder @AllArgsConstructor @NoArgsConstructor
public class SolarSystem implements Serializable {

  private String id;
  private LocalDate commissioning;
  private Double installedGrossPowerkWp;
  private Double installedNetPowerkWp;
  private Instant lastChange;
  private Status status;
  private String operatorName;
  private String name;

  public enum Status {
    IN_OPERATION,
    PERMANENTLY_SHUT_DOWN,
    NONE
  }

  public Integer getCommissioningYear() {
    return commissioning.getYear();
  }

  public Month getCommissioningMonth() {
    return commissioning.getMonth();
  }

  public boolean isInOperation() {
    return status.equals(IN_OPERATION);
  }
}
