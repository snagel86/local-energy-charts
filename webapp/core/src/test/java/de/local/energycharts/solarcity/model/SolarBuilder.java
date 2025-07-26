package de.local.energycharts.solarcity.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.PERMANENTLY_SHUT_DOWN;
import static java.time.Month.JANUARY;

public class SolarBuilder {

  private final Set<SolarSystem> solarSystems = new HashSet<>();
  private Integer id = 0;
  private Integer year = 2022;
  private Month month = JANUARY;

  public SolarBuilder withYear(int year) {
    this.year = year;
    return this;
  }

  public SolarBuilder withMonth(Month month) {
    this.month = month;
    return this;
  }

  public SolarBuilder addBalkonkraftwerksWith06kWp(int count) {
    for (int i = 0; i < count; ++i) {
      solarSystems.add(createSolarSystem(0.6));
    }
    return this;
  }

  public SolarBuilder addHomesWith5kWp(int count) {
    for (int i = 0; i < count; ++i) {
      solarSystems.add(createSolarSystem(5.0));
    }
    return this;
  }

  public SolarBuilder addPermanentlyShutDownHomes(int count) {
    for (int i = 0; i < count; ++i) {
      solarSystems.add(createSolarSystem(5.0, PERMANENTLY_SHUT_DOWN));
    }
    return this;
  }

  public SolarBuilder addApartmentBuildingsWith25kWp(int count) {
    for (int i = 0; i < count; ++i) {
      solarSystems.add(createSolarSystem(25.0));
    }
    return this;
  }

  public SolarBuilder addSchoolsWith100kWp(int count) {
    for (int i = 0; i < count; ++i) {
      solarSystems.add(createSolarSystem(100.0));
    }
    return this;
  }

  public SolarBuilder addSchoolsWith250kWp(int count) {
    for (int i = 0; i < count; ++i) {
      solarSystems.add(createSolarSystem(250));
    }
    return this;
  }

  public Set<SolarSystem> build() {
    return solarSystems;
  }

  private SolarSystem createSolarSystem(double installedNetPowerkWp) {
    return new SolarSystem()
        .setId(String.valueOf(id++))
        .setStatus(IN_OPERATION)
        .setInstalledNetPowerkWp(installedNetPowerkWp)
        .setCommissioning(LocalDate.of(year, month, 1));
  }

  private SolarSystem createSolarSystem(double installedNetPowerkWp, SolarSystem.Status status) {
    return new SolarSystem()
        .setId(String.valueOf(id++))
        .setStatus(status)
        .setInstalledNetPowerkWp(installedNetPowerkWp)
        .setCommissioning(LocalDate.of(year, 1, 1));
  }
}
