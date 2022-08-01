package de.local.energycharts.api.v1.statistic.service;

import de.local.energycharts.api.v1.statistic.model.AdditionOfSolarInstallationsResponse;
import de.local.energycharts.api.v1.statistic.model.OperatorOverviewResponse;
import de.local.energycharts.api.v1.statistic.model.SolarCityOverviewResponse;
import de.local.energycharts.api.v1.statistic.model.mapper.AdditionOfSolarInstallationsMapper;
import de.local.energycharts.api.v1.statistic.model.mapper.OperatorOverviewMapper;
import de.local.energycharts.api.v1.statistic.model.mapper.SolarCityOverviewMapper;
import de.local.energycharts.core.service.SolarCityStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticApiService {

  private final SolarCityStatisticService solarCityStatisticService;

  private final SolarCityOverviewMapper solarOverviewMapper;
  private final OperatorOverviewMapper operatorOverviewMapper;
  private final AdditionOfSolarInstallationsMapper additionOfSolarInstallationsMapper;

  public Mono<SolarCityOverviewResponse> createSolarCityOverview(String city) {
    return solarCityStatisticService.createSolarCityOverview(city)
        .map(solarOverviewMapper::mapToResponse);
  }

  public Flux<OperatorOverviewResponse> createOperatorOverviews(String city) {
    return solarCityStatisticService.createOperatorOverviews(city)
        .map(operatorOverviewMapper::mapToResponse);
  }

  public Flux<AdditionOfSolarInstallationsResponse> createAnnualAdditionOfSolarInstallations(
      String city,
      boolean previousSolarInstallationsOnly
  ) {
    return solarCityStatisticService.createAnnualAdditionOfSolarInstallations(
        city,
        previousSolarInstallationsOnly
    ).map(additionOfSolarInstallationsMapper::mapToResponse);
  }
}
