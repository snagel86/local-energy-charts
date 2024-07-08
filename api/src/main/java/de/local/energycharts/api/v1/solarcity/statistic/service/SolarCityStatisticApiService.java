package de.local.energycharts.api.v1.solarcity.statistic.service;

import de.local.energycharts.api.v1.solarcity.statistic.model.common.SolarCityOverviewResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.common.mapper.SolarCityOverviewMapper;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.service.SolarCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticApiService {

  private final SolarCityService solarCityService;
  private final SolarCityOverviewMapper solarOverviewMapper;

  public Mono<SolarCityOverviewResponse> createOverview(String city) {
    return solarCityService.getCachedSolarCity(city)
        .map(SolarCity::calculateSolarCityOverview)
        .map(solarOverviewMapper::mapToResponse);
  }
}
