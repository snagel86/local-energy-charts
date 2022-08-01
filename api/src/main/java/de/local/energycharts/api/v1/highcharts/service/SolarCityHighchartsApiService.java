package de.local.energycharts.api.v1.highcharts.service;

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.groupingBy;

import de.local.energycharts.api.v1.highcharts.model.Series;
import de.local.energycharts.api.v1.highcharts.model.SolarCityHighchartsResponse;
import de.local.energycharts.api.v1.highcharts.model.mapper.HighchartsAdditionOfSolarInstallationsMapper;
import de.local.energycharts.core.model.SolarCity;
import de.local.energycharts.core.model.statistic.AdditionOfSolarInstallations;
import de.local.energycharts.core.service.SolarCityStatisticService;
import de.local.energycharts.mastr.service.MastrSolarService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityHighchartsApiService {

  private final MastrSolarService mastrSolarService;
  private final SolarCityStatisticService solarCityStatisticService;

  private final HighchartsAdditionOfSolarInstallationsMapper highchartsAdditionOfSolarInstallationsMapper;

  public Mono<SolarCityHighchartsResponse> createAnnualAdditionOfSolarInstallationsChart(
      String city,
      boolean previousSolarInstallationsOnly
  ) {
    return solarCityStatisticService.getSolarCity(city)
        .map(solarCity -> createSolarCityHighchartsResponse(solarCity, previousSolarInstallationsOnly, 10));
  }

  public Mono<SolarCityHighchartsResponse> createTemporaryAnnualAdditionOfSolarInstallationsChart(
      String cityName, Set<Integer> postcodes,
      Double totalSolarPotentialMWp, Integer totalSolarPotentialTargetYear
  ) {
    return mastrSolarService
        .createSolarCityTemporary(
            cityName, postcodes,
            totalSolarPotentialMWp, totalSolarPotentialTargetYear
        )
        .map(solarCity -> createSolarCityHighchartsResponse(solarCity, false, 20));
  }

  private SolarCityHighchartsResponse createSolarCityHighchartsResponse(
      SolarCity solarCity,
      boolean previousSolarInstallationsOnly,
      int periodOfLastYears
  ) {
    // data
    var response = SolarCityHighchartsResponse.builder()
        .cityName(solarCity.getName())
        .data(solarCity.calculateAnnualAdditionOfSolarInstallations().stream()
            .filter(addition -> addition.getYear() >= now().getYear() - periodOfLastYears)
            .filter(addition -> !previousSolarInstallationsOnly || addition.getYear() <= now().getYear())
            .map(highchartsAdditionOfSolarInstallationsMapper::mapToResponse).toList())
        .build();

    // drilldownData
    solarCity.calculateMonthlyAdditionOfSolarInstallations().stream()
        .collect(groupingBy(AdditionOfSolarInstallations::getYear))
        .forEach((year, monthlyAdditionsByYear) ->
            response.addDrillDownSeries(Series.builder()
                .id(year.toString())
                .name(year.toString())
                .data(monthlyAdditionsByYear.stream()
                    .map(highchartsAdditionOfSolarInstallationsMapper::mapToResponse).toList()
                ).build()
            )
        );

    // overview
    var solarOverview = solarCity.calculateSolarCityOverview();
    response.setTotalSolarInstallations(solarOverview.getTotalSolarInstallations());

    return response;
  }
}
