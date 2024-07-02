package de.local.energycharts.infrastructure.solarcity.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Document
@Data
public class MongoSolarSystem implements Serializable {

  @Id
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
}
