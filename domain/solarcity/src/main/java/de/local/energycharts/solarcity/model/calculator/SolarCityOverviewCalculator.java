package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Set;

@RequiredArgsConstructor
public class SolarCityOverviewCalculator {

  private final Double entireSolarPotentialOnRooftopsMWp;
  private final Integer targetYear;
  private final Set<SolarSystem> solarSystems;
  private final Instant updated;

  public SolarCityOverview calculateSolarCityOverview() {
    var installedRoofMWp = calculateInstalledRoofMWpInOperation();
    var averageRoofSolarMWp = calculateAverageRoofSolarMWp();

    return SolarCityOverview.builder()
        .totalRoofSolarInstallations(calculateTotalRoofSolarInstallations())
        .totalInstalledRoofMWp(installedRoofMWp)
        .usedRoofSolarPotentialPercent(
            entireSolarPotentialOnRooftopsMWp != null ?
                installedRoofMWp / entireSolarPotentialOnRooftopsMWp * 100.0 : -1.0
        )
        .entireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
        .targetYear(targetYear)
        .averageRoofSolarSystemMWp(calculateAverageRoofSolarMWp())
        .roofSolarSystemsToBeInstalledByTheTargetYear(
            entireSolarPotentialOnRooftopsMWp != null ?
                (int) (entireSolarPotentialOnRooftopsMWp / averageRoofSolarMWp) : 0
        )
        .totalBalkonSolarInstallations(calculateTotalBalkonSolarInstallations())
        .totalInstalledBalkonMWp(calculateInstalledBalkonMWpInOperation())
        .updated(updated)
        .build();
  }

  private long calculateTotalRoofSolarInstallations() {
    return solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        // filter out Balkonkraftwerke
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
        .count();
  }

  private Double calculateInstalledRoofMWpInOperation() {
    return solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        // filter out Balkonkraftwerke
        .filter(solarSystem -> solarSystem.getInstalledNetPowerkWp() > 1.0)
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

  private long calculateTotalBalkonSolarInstallations() {
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
}
