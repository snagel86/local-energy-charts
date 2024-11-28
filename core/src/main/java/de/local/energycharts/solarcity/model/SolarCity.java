package de.local.energycharts.solarcity.model;

import de.local.energycharts.solarcity.calculator.AnnualSolarInstallationsCalculator;
import de.local.energycharts.solarcity.calculator.MonthlySolarInstallationsCalculator;
import de.local.energycharts.solarcity.calculator.SolarBuildingPieChartCalculator;
import de.local.energycharts.solarcity.calculator.OverviewCalculator;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static de.local.energycharts.solarcity.model.Time.now;

@AggregateRoot
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

  public Overview calculateSolarCityOverview() {
    return new OverviewCalculator(this)
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
