package de.local.energycharts.core.model;

import static java.time.Instant.now;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import de.local.energycharts.core.model.statistic.AdditionOfSolarInstallations;
import de.local.energycharts.core.model.statistic.FutureAdditionOfSolarInstallationsCalculator;
import de.local.energycharts.core.model.statistic.OperatorOverview;
import de.local.energycharts.core.model.statistic.OperatorOverviewCalculator;
import de.local.energycharts.core.model.statistic.SolarCityOverview;
import de.local.energycharts.core.model.statistic.SolarCityOverviewCalculator;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document
public class SolarCity implements Serializable {

  @Id
  private String id;
  @Indexed(unique = true)
  private String name;
  private Instant created;
  private Instant updated;
  private Double totalSolarPotentialMWp;
  private Integer targetYear;
  private Set<District> districts = new HashSet<>();

  @Transient
  private Integer currentYear;

  public static SolarCity createNewSolarCity(String name) {
    return new SolarCity(LocalDate.now().getYear())
        .setName(name)
        .setCreated(now());
  }

  SolarCity() {
    currentYear = LocalDate.now().getYear();
  }

  SolarCity(int currentYear) {
    this.currentYear = currentYear;
  }

  public int calculateTotalNumberOfSolarInstallations() {
    return districts.stream().mapToInt(District::countAllSolarSystems).sum();
  }

  public SolarCityOverview calculateSolarCityOverview() {
    return new SolarCityOverviewCalculator(
        totalSolarPotentialMWp,
        calculateAlreadyInstalledMWp(),
        getSolarSystems(),
        updated
    ).calculateSolarCityOverview();
  }

  public Set<OperatorOverview> calculateOperatorOverview() {
    return new OperatorOverviewCalculator(districts).calculateOperatorOverview();
  }

  public List<AdditionOfSolarInstallations> calculateAnnualAdditionOfSolarInstallations() {
    Map<Integer, AdditionOfSolarInstallations> additions = new HashMap<>();

    districts.stream()
        .map(District::calculateAnnualAdditionOfSolarInstallations).flatMap(Collection::stream)
        .collect(groupingBy(AdditionOfSolarInstallations::getYear))
        .forEach((year, additionsByYear) -> additions.put(year, addUpAll(additionsByYear)));

    if (isTotalSolarPotentialAndTargetYearSpecified()) {
      List<AdditionOfSolarInstallations> additionsDoneYet = additions.values().stream().toList();
      additions.putAll(calculateFutureAnnualAdditionOfSolarSystems(additionsDoneYet));
    }
    return additions.values().stream().sorted().toList();
  }

  public List<AdditionOfSolarInstallations> calculateMonthlyAdditionOfSolarInstallations() {
    Map<String, AdditionOfSolarInstallations> additions = new HashMap<>();

    districts.stream()
        .map(District::calculateMonthlyAdditionOfSolarInstallations).flatMap(Collection::stream)
        .collect(groupingBy(AdditionOfSolarInstallations::getYearAndMonth))
        .forEach((yyyyMM, additionsByYYYYMM) -> additions.put(yyyyMM, addUpAll(additionsByYYYYMM)));
    return additions.values().stream().sorted().toList();
  }

  private AdditionOfSolarInstallations addUpAll(List<AdditionOfSolarInstallations> additions) {
    return additions.stream().reduce(AdditionOfSolarInstallations::add).orElse(null);
  }

  private boolean isTotalSolarPotentialAndTargetYearSpecified() {
    return totalSolarPotentialMWp != null && targetYear != null;
  }

  private Map<Integer, AdditionOfSolarInstallations> calculateFutureAnnualAdditionOfSolarSystems(
      List<AdditionOfSolarInstallations> additionsDoneYet
  ) {
    return new FutureAdditionOfSolarInstallationsCalculator(
        totalSolarPotentialMWp,
        calculateAlreadyInstalledMWp(),
        targetYear,
        additionsDoneYet,
        currentYear
    ).calculateAnnualAdditions();
  }

  double calculateAlreadyInstalledMWp() {
    return districts.stream().mapToDouble(District::calculateTotalInstalledMWp).sum();
  }

  private Set<SolarSystem> getSolarSystems() {
    return districts.stream()
        .map(District::getSolarSystems)
        .flatMap(Collection::stream).collect(Collectors.toSet()).stream()
        .filter(SolarSystem::isInOperation)
        .collect(Collectors.toSet());
  }
}
