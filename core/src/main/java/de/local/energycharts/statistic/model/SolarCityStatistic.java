package de.local.energycharts.statistic.model;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.statistic.calculator.AnnualSolarInstallationsCalculator;
import de.local.energycharts.statistic.calculator.MonthlySolarInstallationsCalculator;
import de.local.energycharts.statistic.calculator.SolarBuildingPieChartCalculator;
import de.local.energycharts.statistic.calculator.SolarCityOverviewCalculator;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Set;

@Data
@Accessors(chain = true)
public class SolarCityStatistic {

  private String name;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  private Set<SolarSystem> solarSystems;
  private Instant updated;

  public SolarCityOverview calculateSolarCityOverview() {
    return new SolarCityOverviewCalculator(this)
        .calculateSolarCityOverview();
  }

  public SolarBuildingPieChart calculateSolarBuildingPieChart() {
    return new SolarBuildingPieChartCalculator(solarSystems)
        .calculatePieChart();
  }

  public MonthlySolarInstallations calculateMonthlySolarInstallations() {
    return new MonthlySolarInstallationsCalculator(solarSystems)
        .calculateMonthlyInstallations();
  }

  public AnnualSolarInstallations calculateAnnualSolarInstallations() {
    return new AnnualSolarInstallationsCalculator(this)
        .calculateAnnualSolarInstallations();
  }
}
