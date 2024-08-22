package de.local.energycharts.api.v1.solarcity.create.service;

import de.local.energycharts.api.v1.solarcity.create.model.CreateSolarCityRequest;
import de.local.energycharts.api.v1.solarcity.create.model.SolarCityCreatedResponse;
import de.local.energycharts.api.v1.solarcity.create.model.SolarCityResponse;
import de.local.energycharts.api.v1.solarcity.create.model.mapper.SolarCityCreatedMapper;
import de.local.energycharts.api.v1.solarcity.create.model.mapper.SolarCityMapper;
import de.local.energycharts.solarcity.service.SolarCityService;
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
  private final SolarCityMapper solarCityMapper;

  public Mono<SolarCityCreatedResponse> createOrUpdateSolarCity(@RequestBody CreateSolarCityRequest request) {
    if (request.getMunicipalityKey() != null) {
      return solarCityService.createOrUpdateSolarCity(
          request.getCityName(),
          request.getMunicipalityKey(),
          request.getEntireSolarPotentialOnRooftopsMWp(), request.getTargetYear()
      ).map(solarCityCreatedMapper::mapToResponse);
    }

    return solarCityService.createOrUpdateSolarCity(
        request.getCityName(),
        request.getEntireSolarPotentialOnRooftopsMWp(), request.getTargetYear()
    ).map(solarCityCreatedMapper::mapToResponse);
  }

  public Flux<SolarCityResponse> getAllSolarCities() {
    return solarCityService.getAllSolarCities()
        .map(solarCityMapper::mapToResponse);
  }

  public Flux<Integer> getAllPostcodes(String solarCityName) {
    return solarCityService.getAllPostcodes(solarCityName);
  }
}
