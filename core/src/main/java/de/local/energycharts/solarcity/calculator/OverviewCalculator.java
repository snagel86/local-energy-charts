package de.local.energycharts.solarcity.calculator;

import de.local.energycharts.solarcity.model.Overview;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.SolarSystem;

import java.time.Instant;
import java.util.Set;

public class OverviewCalculator {

  private final Double entireSolarPotentialOnRooftopsMWp;
  private final Integer targetYear;
  private final Set<SolarSystem> solarSystems;
  private final Instant updated;

  public OverviewCalculator(SolarCity solarCity) {
    entireSolarPotentialOnRooftopsMWp = solarCity.getEntireSolarPotentialOnRooftopsMWp();
    targetYear = solarCity.getTargetYear();
    solarSystems = solarCity.getSolarSystems();
    updated = solarCity.getUpdated();
  }

  public Overview calculateSolarCityOverview() {
    var installedRooftopMWp = calculateInstalledRooftopMWp();
    var averageRoofSolarMWp = calculateAverageRoofSolarMWp();

    return Overview.builder()
        .rooftopSolarSystems(countActiveRooftopSolarSystems())
        .installedRooftopMWp(installedRooftopMWp)
        .balkonSolarSystems(countBalkonSolarSystems())
        .installedBalkonMWp(calculateInstalledActiveBalkonMWp())
        .usedRoofSolarPotentialPercent(
            entireSolarPotentialOnRooftopsMWp != null ?
                installedRooftopMWp / entireSolarPotentialOnRooftopsMWp * 100.0 : -1.0
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

  long countActiveRooftopSolarSystems() {
    return solarSystems.stream()
        .filter(SolarSystem::isActive)
        // filter out Balkonkraftwerke
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
        .count();
  }

  Double calculateInstalledRooftopMWp() {
    return solarSystems.stream()
        .filter(SolarSystem::isActive)
        // filter out Balkonkraftwerke
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
        .mapToDouble(SolarSystem::getInstalledNetPowerkWp)
        .sum() / 1000.0; // kWp -> MWp
  }

  private long countBalkonSolarSystems() {
    return solarSystems.stream()
        .filter(SolarSystem::isActive)
        // Balkonkraftwerke only
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() <= 1.0)
        .count();
  }

  private Double calculateInstalledActiveBalkonMWp() {
    return solarSystems.stream()
        .filter(SolarSystem::isActive)
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
