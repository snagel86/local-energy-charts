package de.local.energycharts.api.v1.mastr.service;

import de.local.energycharts.mastr.service.MastrSolarService;
import de.local.energycharts.api.v1.mastr.model.CreateSolarCityRequest;
import de.local.energycharts.api.v1.mastr.model.mapper.SolarCityCreatedMapper;
import de.local.energycharts.api.v1.mastr.model.SolarCityCreatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MastrApiService {

  private final MastrSolarService mastrSolarService;
  private final SolarCityCreatedMapper solarCityCreatedMapper;

  public Mono<SolarCityCreatedResponse> createSolarCity(@RequestBody CreateSolarCityRequest request) {
    return mastrSolarService.createOrUpdateSolarCity(
        request.getCityName(), request.getPostcodes(),
        request.getTotalSolarPotentialMWp(), request.getTotalSolarPotentialTargetYear()
    ).map(solarCityCreatedMapper::mapToResponse);
  }
}
