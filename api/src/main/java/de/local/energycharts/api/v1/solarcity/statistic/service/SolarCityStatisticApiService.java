package de.local.energycharts.api.v1.solarcity.statistic.service;

import de.local.energycharts.api.v1.solarcity.statistic.model.common.SolarCityOverviewResponse;
import de.local.energycharts.api.v1.solarcity.statistic.model.common.mapper.SolarCityOverviewMapper;
import de.local.energycharts.solarcity.service.SolarCityStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityStatisticApiService {

  private final SolarCityStatisticService solarCityStatisticService;
  private final SolarCityOverviewMapper solarOverviewMapper;

  public Mono<SolarCityOverviewResponse> createOverview(String id) {
    return solarCityStatisticService.calculateSolarCityOverview(id)
        .map(solarOverviewMapper::mapToResponse);
  }
}
