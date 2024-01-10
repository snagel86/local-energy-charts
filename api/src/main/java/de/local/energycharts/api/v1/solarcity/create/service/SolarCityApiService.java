package de.local.energycharts.api.v1.solarcity.create.service;

import de.local.energycharts.solarcity.service.SolarCityService;
import de.local.energycharts.api.v1.solarcity.create.model.CreateSolarCityRequest;
import de.local.energycharts.api.v1.solarcity.create.model.mapper.SolarCityCreatedMapper;
import de.local.energycharts.api.v1.solarcity.create.model.SolarCityCreatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityApiService {

  private final SolarCityService solarCityService;
  private final SolarCityCreatedMapper solarCityCreatedMapper;

  public Mono<SolarCityCreatedResponse> createSolarCity(@RequestBody CreateSolarCityRequest request) {
    return solarCityService.createOrUpdateSolarCity(
        request.getCityName(),
        request.getEntireSolarPotentialOnRooftopsMWp(), request.getTargetYear()
    ).map(solarCityCreatedMapper::mapToResponse);
  }

  public Flux<Integer> getAllPostcodes(String solarCityIdOrName) {
    return solarCityService.getAllPostcodes(solarCityIdOrName);
  }
}
