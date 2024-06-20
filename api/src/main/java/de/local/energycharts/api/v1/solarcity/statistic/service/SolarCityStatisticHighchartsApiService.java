package de.local.energycharts.api.v1.solarcity.statistic.service;

import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.AnnualAdditionOfSolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.ColumnMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.MonthlySolarInstallationsChartMapper;
import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper.SolarBuildingPieChartMapper;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.service.SolarCityService;
import de.local.energycharts.solarcity.service.SolarCityStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static de.local.energycharts.api.v1.solarcity.statistic.service.RangeOfPreviouslySolarInstallationsCalculator.calculateRange;
import static de.local.energycharts.solarcity.model.Time.currentYear;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticHighchartsApiService {

  private final SolarCityService solarCityService;
  private final SolarCityStatisticService solarCityStatisticService;
  private final ColumnMapper columnMapper;
  private final SolarBuildingPieChartMapper solarBuildingPieChartMapper;
  private final MonthlySolarInstallationsChartMapper monthlySolarInstallationsChartMapper;

  public Mono<AnnualAdditionOfSolarInstallationsChartResponse> createAnnualAdditionOfSolarInstallationsChart(
      String city,
      boolean previousSolarInstallationsOnly
  ) {
    return solarCityStatisticService.getSolarCity(city)
        .map(solarCity -> createColumnChartResponse(
            solarCity, previousSolarInstallationsOnly,
            calculateRange(previousSolarInstallationsOnly, solarCity.getTargetYear()).inYears()
        ));
  }

  public Mono<AnnualAdditionOfSolarInstallationsChartResponse> createTemporaryAnnualAdditionOfSolarInstallationsChart(
      String cityName,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityService
        .createSolarCityTemporary(
            cityName,
            entireSolarPotentialOnRooftopsMWp, targetYear
        )
        .map(solarCity -> createColumnChartResponse(solarCity, false, 20))
        .map(this::throwErrorWhenEmpty);
  }

  private AnnualAdditionOfSolarInstallationsChartResponse createColumnChartResponse(
      SolarCity solarCity,
      boolean previousSolarInstallationsOnly,
      int rangeInYearsOfPreviouslySolarInstallations
  ) {
    // data
    var response = AnnualAdditionOfSolarInstallationsChartResponse.builder()
        .cityName(solarCity.getName())
        .columns(solarCity.calculateAnnualAdditionOfSolarInstallations().stream()
            .filter(addition -> addition.getYear() >= currentYear() - rangeInYearsOfPreviouslySolarInstallations)
            .filter(addition -> !previousSolarInstallationsOnly || addition.getYear() <= currentYear())
            .map(columnMapper::mapToColumn).toList())
        .build();

    // overview
    var solarOverview = solarCity.calculateSolarCityOverview();
    response
        .setTotalSolarInstallations(solarOverview.getRooftopSolarSystemsInOperation())
        .setTotalInstalledMWp(solarOverview.getInstalledRooftopMWpInOperation());

    return response;
  }

  private AnnualAdditionOfSolarInstallationsChartResponse throwErrorWhenEmpty(AnnualAdditionOfSolarInstallationsChartResponse response) {
    if (response.isEmpty()) {
      throw new IllegalStateException("city does not exist!");
    } else {
      return response;
    }
  }

  public Mono<SolarBuildingPieChartResponse> createSolarBuildingPieChart(String city) {
    return solarCityStatisticService.createSolarBuildingPieChart(city)
        .map(solarBuildingPieChartMapper::mapToResponse);
  }

  public Mono<MonthlySolarInstallationsChartResponse> createMonthlySolarInstallationsChart(String city) {
    return solarCityStatisticService.createMonthlySolarInstallations(city)
        .map(monthlySolarInstallationsChartMapper::mapToResponse);
  }
}
