package de.local.energycharts.solarcity.model;

import de.local.energycharts.solarcity.model.calculator.*;
import de.local.energycharts.solarcity.model.statistic.AdditionOfSolarInstallations;
import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.local.energycharts.solarcity.model.Time.now;
import static java.util.stream.Collectors.toSet;

@Data
@Accessors(chain = true)
@Document
public class SolarCity implements Serializable {

  @Id
  private String id;
  @Indexed(unique = true)
  private String name;
  private String municipalityKey;
  private Instant created;
  private Instant updated;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  private Set<SolarSystem> solarSystems;

  public static SolarCity createNewSolarCity(String name) {
    return new SolarCity()
        .setName(name)
        .setCreated(now());
  }

  public static SolarCity createNewSolarCity(
      String name,
      String municipalityKey
  ) {
    return new SolarCity()
        .setName(name)
        .setMunicipalityKey(municipalityKey)
        .setCreated(now());
  }

  public int calculateTotalNumberOfSolarInstallations() {
    return solarSystems.stream().filter(SolarSystem::isInOperation).toList().size();
  }

  public SolarCityOverview calculateSolarCityOverview() {
    return new SolarCityOverviewCalculator(
        entireSolarPotentialOnRooftopsMWp,
        targetYear,
        solarSystems,
        updated
    ).calculateSolarCityOverview();
  }

  public SolarBuildingPieChart calculateSolarBuildingPieChart() {
    return new SolarBuildingPieChartCalculator(solarSystems).calculatePieChart();
  }

  public MonthlySolarInstallations calculateMonthlySolarInstallations() {
    return new MonthlySolarInstallationsCalculator(solarSystems).calculatorMonthlyInstallations();
  }

  public List<AdditionOfSolarInstallations> calculateAnnualAdditionOfSolarInstallations() {
    var additions = calculateAnnualAdditionOfSolarSystems();

    if (isTotalSolarPotentialAndTargetYearSpecified()) {
      var futureAdditions = calculateFutureAnnualAdditionOfSolarSystems(additions);
      additions.addAll(futureAdditions);
    }

    return additions.stream()
        .map(addition -> addition.setCityName(name))
        .sorted()
        .toList();
  }

  private List<AdditionOfSolarInstallations> calculateAnnualAdditionOfSolarSystems() {
    return new AdditionOfSolarInstallationsCalculator(solarSystems).calculateAnnualAdditions();
  }

  private boolean isTotalSolarPotentialAndTargetYearSpecified() {
    return entireSolarPotentialOnRooftopsMWp != null && targetYear != null;
  }

  private List<AdditionOfSolarInstallations> calculateFutureAnnualAdditionOfSolarSystems(
      List<AdditionOfSolarInstallations> additionsDoneYet
  ) {
    return new FutureAdditionOfSolarInstallationsCalculator(
        entireSolarPotentialOnRooftopsMWp,
        targetYear,
        solarSystems,
        additionsDoneYet
    ).calculateAnnualAdditions();
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
