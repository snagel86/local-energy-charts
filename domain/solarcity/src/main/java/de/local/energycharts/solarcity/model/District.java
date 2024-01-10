package de.local.energycharts.solarcity.model;

import de.local.energycharts.solarcity.model.statistic.AdditionOfSolarInstallations;
import de.local.energycharts.solarcity.model.calculator.AdditionOfSolarInstallationsCalculator;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@Builder
public class District implements Serializable {

  private Integer postcode;
  private Set<SolarSystem> solarSystems;

  public Integer countAllSolarSystems() {
    return solarSystems.stream().filter(SolarSystem::isInOperation).toList().size();
  }

  public Double calculateTotalInstalledMWp() {
    double totalInstalledkWp = solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        .mapToDouble(SolarSystem::getInstalledNetPowerkWp)
        .sum();

    return totalInstalledkWp / 1000.0; // kWp -> MWp
  }

  public List<AdditionOfSolarInstallations> calculateAnnualAdditionOfSolarInstallations() {
    return new AdditionOfSolarInstallationsCalculator(solarSystems).calculateAnnualAdditions();
  }

  public boolean hasAtLeastOneSolarSystem() {
    return solarSystems != null && !solarSystems.isEmpty();
  }
}
