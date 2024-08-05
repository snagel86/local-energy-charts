package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;

import java.time.Instant;
import java.util.Set;

public class SolarCityOverviewCalculator {

  private final Double entireSolarPotentialOnRooftopsMWp;
  private final Integer targetYear;
  private final Set<SolarSystem> solarSystems;
  private final Instant updated;

  public SolarCityOverviewCalculator(SolarCity solarCity) {
    entireSolarPotentialOnRooftopsMWp = solarCity.getEntireSolarPotentialOnRooftopsMWp();
    targetYear = solarCity.getTargetYear();
    solarSystems = solarCity.getAllSolarSystems();
    updated = solarCity.getUpdated();
  }

  public SolarCityOverview calculateSolarCityOverview() {
    var installedRooftopMWpInOperation = calculateInstalledRooftopMWpInOperation();
    var averageRoofSolarMWp = calculateAverageRoofSolarMWp();

    return SolarCityOverview.builder()
        .rooftopSolarSystemsInOperation(calculateRooftopSolarSystemsInOperation())
        .installedRooftopMWpInOperation(installedRooftopMWpInOperation)
        .balkonSolarSystemsInOperation(calculateBalkonSolarSystemsInOperation())
        .installedBalkonMWpInOperation(calculateInstalledBalkonMWpInOperation())
        .usedRoofSolarPotentialPercent(
            entireSolarPotentialOnRooftopsMWp != null ?
                installedRooftopMWpInOperation / entireSolarPotentialOnRooftopsMWp * 100.0 : -1.0
        )
        .entireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
        .targetYear(targetYear)
        .averageRoofSolarSystemMWp(calculateAverageRoofSolarMWp())
        .roofSolarSystemsToBeInstalledByTheTargetYear(
            entireSolarPotentialOnRooftopsMWp != null ?
                (int) (entireSolarPotentialOnRooftopsMWp / averageRoofSolarMWp) : 0
        )
        .updated(updated)
        .build();
  }

  private long calculateRooftopSolarSystemsInOperation() {
    return solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        // filter out Balkonkraftwerke
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
        .count();
  }

  private Double calculateInstalledRooftopMWpInOperation() {
    return solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        // filter out Balkonkraftwerke
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
        .mapToDouble(SolarSystem::getInstalledNetPowerkWp)
        .sum() / 1000.0; // kWp -> MWp
  }

  private long calculateBalkonSolarSystemsInOperation() {
    return solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        // Balkonkraftwerke only
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 1.0)
        .count();
  }

  private Double calculateInstalledBalkonMWpInOperation() {
    return solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        // Balkonkraftwerke only
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 1.0)
        .mapToDouble(SolarSystem::getInstalledNetPowerkWp)
        .sum() / 1000.0; // kWp -> MWp
  }

  Double calculateAverageRoofSolarMWp() {
    return solarSystems.stream()
        // filter out Balkonkraftwerke, as they must be subtracted from rooftop solar potential
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
        .mapToDouble(SolarSystem::getInstalledNetPowerkWp)
        .average()
        .orElse(0.0) / 1000.0; // kWp -> MWp
  }
}
