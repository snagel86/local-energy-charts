package de.local.energycharts.api.v1.solarcity.admin;

import de.local.energycharts.api.v1.solarcity.admin.model.CreateSolarCityRequest;
import de.local.energycharts.api.v1.solarcity.admin.model.SolarCityCreatedResponse;
import de.local.energycharts.api.v1.solarcity.admin.model.SolarCityResponse;
import de.local.energycharts.api.v1.solarcity.admin.model.mapper.SolarCityCreatedMapper;
import de.local.energycharts.api.v1.solarcity.admin.model.mapper.SolarCityMapper;
import de.local.energycharts.solarcity.ports.in.AdministrateSolarCity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SolarCityApiService {

  private final AdministrateSolarCity administrateSolarCity;
  private final SolarCityCreatedMapper solarCityCreatedMapper;
  private final SolarCityMapper solarCityMapper;

  public Mono<SolarCityCreatedResponse> createOrUpdate(@RequestBody CreateSolarCityRequest request) {
    return administrateSolarCity.createOrUpdate(
        request.getCityName(),
        request.getMunicipalityKey(),
        request.getEntireSolarPotentialOnRooftopsMWp(), request.getTargetYear()
    ).map(solarCityCreatedMapper::mapToResponse);
  }

  public Flux<SolarCityCreatedResponse> updateAll() {
    return administrateSolarCity.updateAll()
        .map(solarCityCreatedMapper::mapToResponse);
  }

  public Flux<SolarCityResponse> getAll() {
    return administrateSolarCity.getAll()
        .map(solarCityMapper::mapToResponse);
  }

  public Flux<Integer> getAllPostcodes(String id) {
    return administrateSolarCity.getAllPostcodes(id);
  }

  public void delete(String id) {
    administrateSolarCity.delete(id);
  }
}
