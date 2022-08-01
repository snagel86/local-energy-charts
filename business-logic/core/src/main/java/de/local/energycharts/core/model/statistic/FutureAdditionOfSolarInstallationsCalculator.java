package de.local.energycharts.core.model.statistic;

import static java.lang.Math.exp;
import static java.util.Comparator.comparing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FutureAdditionOfSolarInstallationsCalculator {

  private final Double totalSolarPotentialMWp;
  private final Double alreadyInstalledMWp;
  private final Integer targetYear;
  private final List<AdditionOfSolarInstallations> additionsDoneYet;
  private final Integer currentYear;

  public Map<Integer, AdditionOfSolarInstallations> calculateAnnualAdditions() {
    Map<Integer, Double> annualFutureDistribution = calculateAnnualFutureDistribution();
    return applyToSolarPotentialStillAvailable(annualFutureDistribution, additionsDoneYet);
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
    int nextYear = currentYear + 1;

    for (double t = -3.0; t <= 6.0; t += 0.01) {
      Integer year = (int)
          Math.round((targetYear - nextYear) / 9.0 * (t + 3.0)) + nextYear;
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
   * potential yet to be installed is calculated on the total area under the curve. Since the
   * installation in the current year has not been completed, and we should not fall behind what has
   * been achieved, the previous year is assumed to be the offset.
   *
   * @param annualFutureDistribution the distribution to transfer
   * @param additionsDoneYet         the additions, already done
   * @return annual future installation addition of solar systems
   */
  private Map<Integer, AdditionOfSolarInstallations> applyToSolarPotentialStillAvailable(
      Map<Integer, Double> annualFutureDistribution,
      List<AdditionOfSolarInstallations> additionsDoneYet
  ) {
    Map<Integer, AdditionOfSolarInstallations> futureAdditions = new HashMap<>();
    double annualInstalledMWpPreviousYear = getAnnualInstalledMWpFromPreviousYear(additionsDoneYet);
    double totalAreaUnderDistribution = annualFutureDistribution.values().stream().mapToDouble(d -> d).sum();
    double yetToBeInstalled = totalSolarPotentialMWp - alreadyInstalledMWp;

    annualFutureDistribution.forEach((year, annualValue) ->
        futureAdditions.put(
            year,
            AdditionOfSolarInstallations.builder()
                .year(year)
                .totalInstalledMWp(
                    annualValue *
                        (yetToBeInstalled - (annualInstalledMWpPreviousYear
                            * (annualFutureDistribution.size()))) // subtract all offsets
                        / totalAreaUnderDistribution
                        + annualInstalledMWpPreviousYear // offset
                )
                .calculatedInFuture(true).build()
        )
    );
    return futureAdditions;
  }

  Double getAnnualInstalledMWpFromPreviousYear(List<AdditionOfSolarInstallations> pastAdditions) {
    return pastAdditions.stream()
        .filter(pastAddition -> !pastAddition.getYear().equals(currentYear))
        .sorted(comparing(AdditionOfSolarInstallations::getYear).reversed())
        .mapToDouble(AdditionOfSolarInstallations::getTotalInstalledMWp)
        .findFirst()
        .orElse(0.0);
  }
}
