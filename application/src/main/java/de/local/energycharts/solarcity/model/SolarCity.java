package de.local.energycharts.solarcity.model;

import de.local.energycharts.solarcity.model.calculator.AnnualSolarInstallationsCalculator;
import de.local.energycharts.solarcity.model.calculator.MonthlySolarInstallationsCalculator;
import de.local.energycharts.solarcity.model.calculator.SolarBuildingPieChartCalculator;
import de.local.energycharts.solarcity.model.calculator.SolarCityOverviewCalculator;
import de.local.energycharts.solarcity.model.statistic.AnnualSolarInstallations;
import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static de.local.energycharts.solarcity.model.Time.now;

@Data
@Accessors(chain = true)
public class SolarCity {

  private String id;
  private String name;
  private String municipalityKey;
  private Instant created;
  private Instant updated;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  private Set<SolarSystem> solarSystems;

  public static SolarCity createNewSolarCity(String name) {
    return createNewSolarCity(name, null);
  }

  public static SolarCity createNewSolarCity(String name, String municipalityKey) {
    return new SolarCity()
        .setName(name)
        .setMunicipalityKey(municipalityKey)
        .setCreated(now());
  }

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

  public Set<SolarSystem> getAllSolarSystems() {
    return solarSystems;
  }

  public List<Integer> getAllPostcodes() {
    return solarSystems.stream()
        .map(SolarSystem::getPostcode)
        .distinct()
        .sorted()
        .toList();
  }

  public boolean wasUpdatedWithin(long amount, ChronoUnit unit) {
    return updated.getEpochSecond() >= now().minus(amount, unit).getEpochSecond();
  }

  public boolean wasNotUpdatedWithin(long amount, ChronoUnit unit) {
    return !wasUpdatedWithin(amount, unit);
  }
}
