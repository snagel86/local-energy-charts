package de.local.energycharts.solarcity.model.calculator;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.model.statistic.AdditionOfSolarInstallations;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static de.local.energycharts.solarcity.model.Time.currentYear;
import static java.lang.Math.exp;
import static java.lang.Math.max;
import static java.util.Comparator.comparing;

@RequiredArgsConstructor
@Builder
public class FutureAdditionOfSolarInstallationsCalculator {

  private final Double entireSolarPotentialOnRooftopsMWp;
  private final Integer targetYear;
  private final Set<SolarSystem> solarSystems;
  private final List<AdditionOfSolarInstallations> additionsDoneYet;

  public List<AdditionOfSolarInstallations> calculateAnnualAdditions() {
    var annualFutureDistribution = calculateAnnualFutureDistribution();
    return applyDistributionToAvailableSolarPotential(annualFutureDistribution);
  }

  /**
   * The future annual distribution is calculated with the logistic function (S-function). The
   * logistic function runs from t = -6 to t = 6 and has its peak at the end. We start at -3 because
   * the installation of solar systems in cities has already begun. The total range, for t = -3 to t
   * = 6, is therefore 9. The result is shifted to the right by 3 for a positive distribution, to
   * represent the future, annual addition of solar installations.
   *
   * @return annual future distribution
   */
  public Map<Integer, Double> calculateAnnualFutureDistribution() {
    Map<Integer, Double> annualFutureDistribution = new HashMap<>();
    int nextYear = currentYear() + 1;

    for (double t = -3.0; t <= 6.0; t += 0.01) {
      Integer year = (int) Math.round((targetYear - nextYear) / 9.0 * (t + 3.0)) + nextYear;
      if (annualFutureDistribution.containsKey(year)) {
        annualFutureDistribution.put(year, 1 / (1 + exp(-t)) + annualFutureDistribution.get(year));
      } else {
        annualFutureDistribution.put(year, 1 / (1 + exp(-t)));
      }
    }
    return annualFutureDistribution;
  }

  /**
   * To apply the logistic function to future, annual addition of solar installations, the solar
   * potential yet to be installed is calculated on the total area under the curve.
   * Since installation in the current year is not yet complete, and we should not fall behind what has been achieved,
   * the previous year is taken as the offset. Unless the installation this year
   * - perhaps towards the end of the year - is already higher.
   * Therefore, the offset is subtracted from the total area under the curve.
   *
   * @param annualFutureDistribution the distribution to transfer
   * @return annual future installation addition of solar systems
   */
  private List<AdditionOfSolarInstallations> applyDistributionToAvailableSolarPotential(
      Map<Integer, Double> annualFutureDistribution
  ) {
    var futureAdditions = new ArrayList<AdditionOfSolarInstallations>();
    double highestLastAnnualInstalledMWp = getHighestLastAnnualInstalledMWp();
    double yetToBeInstalledMWp = calculateYetToBeInstalledMWp();
    double totalAreaUnderDistribution = annualFutureDistribution.values().stream().mapToDouble(d -> d).sum();
    double averageMWpOfAllSolarInstallations = calculateAverageRoofSolarMWp();

    annualFutureDistribution.forEach((year, annualValue) -> {

          var totalInstallMWp = annualValue *
              //                     subtract complete offset
              (yetToBeInstalledMWp - (highestLastAnnualInstalledMWp * (annualFutureDistribution.size())))
              / totalAreaUnderDistribution
              + highestLastAnnualInstalledMWp; // offset
          var numberOfSolarSystems = (int) (totalInstallMWp / averageMWpOfAllSolarInstallations);

          futureAdditions.add(
              AdditionOfSolarInstallations.builder()
                  .year(year)
                  .totalInstalledMWp(totalInstallMWp)
                  .numberOfSolarSystems(numberOfSolarSystems)
                  .build()
          );
        }
    );
    return futureAdditions;
  }

  Double getHighestLastAnnualInstalledMWp() {
    var annualInstalledMWpPreviousYear = getAnnualInstalledMWpFromPreviousYear();
    var annualInstalledMWpCurrentYear = getAnnualInstalledMWpFromCurrentYear();

    return max(annualInstalledMWpPreviousYear, annualInstalledMWpCurrentYear);
  }

  Double getAnnualInstalledMWpFromPreviousYear() {
    return additionsDoneYet.stream()
        .filter(addition -> !addition.getYear().equals(currentYear()))
        .sorted(comparing(AdditionOfSolarInstallations::getYear).reversed())
        .mapToDouble(AdditionOfSolarInstallations::getTotalInstalledMWp)
        .findFirst()
        .orElse(0.0);
  }

  Double getAnnualInstalledMWpFromCurrentYear() {
    return additionsDoneYet.stream()
        .filter(addition -> addition.getYear().equals(currentYear()))
        .mapToDouble(AdditionOfSolarInstallations::getTotalInstalledMWp)
        .findFirst()
        .orElse(0.0);
  }

  Double calculateYetToBeInstalledMWp() {
    return entireSolarPotentialOnRooftopsMWp - calculateInstalledMWpInOperation();
  }

  Double calculateInstalledMWpInOperation() {
    return solarSystems.stream()
        .filter(SolarSystem::isInOperation)
        // filter out Balkonkraftwerke, as they must be subtracted from rooftop solar potential
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
}
