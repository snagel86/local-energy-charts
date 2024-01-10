package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.District;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import de.local.energycharts.solarcity.gateway.MastrGateway;
import de.local.energycharts.solarcity.gateway.OpendatasoftGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class SolarCityService {

  private final SolarCityRepository solarCityRepository;
  private final MastrGateway mastrGateway;
  private final OpendatasoftGateway opendatasoftGateway;

  public Mono<SolarCity> createOrUpdateSolarCity(
      String cityName,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityRepository.findByName(cityName)
        .defaultIfEmpty(SolarCity.createNewSolarCity(cityName))
        .map(solarCity -> solarCity
            .setUpdated(now())
            .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
            .setTargetYear(targetYear)
        )
        .flatMap(solarCity -> opendatasoftGateway.getPostcodes(cityName)
            .flatMap(postcode ->
                mastrGateway.getSolarSystems(postcode)
                    .collect(Collectors.toSet())
                    .map(solarSystems ->
                        District.builder()
                            .postcode(postcode)
                            .solarSystems(solarSystems).build())
            ).collect(Collectors.toSet())
            .map(solarCity::setDistricts)
            .filter(SolarCity::thereIsADistrictWithAtLeastOneSolarInstallation)
            .flatMap(solarCityRepository::save)
        );
  }

  public Mono<SolarCity> updateSolarCity(SolarCity solarCity) {
    return createOrUpdateSolarCity(
        solarCity.getName(),
        solarCity.getEntireSolarPotentialOnRooftopsMWp(),
        solarCity.getTargetYear()
    );
  }

  public Mono<SolarCity> createSolarCityTemporary(
      String cityName,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return Mono.just(
            SolarCity.createNewSolarCity(cityName)
                .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
                .setTargetYear(targetYear)
        )
        .flatMap(solarCity -> opendatasoftGateway.getPostcodes(cityName)
            .flatMap(postcode ->
                mastrGateway.getSolarSystems(postcode)
                    .collect(Collectors.toSet())
                    .map(solarSystems ->
                        District.builder()
                            .postcode(postcode)
                            .solarSystems(solarSystems).build())
            ).collect(Collectors.toSet())
            .map(solarCity::setDistricts)
        );
  }

  public Flux<Integer> getAllPostcodes(String city) {
    return solarCityRepository
        .findByIdOrName(city, city)
        .flatMapIterable(SolarCity::getAllPostcodes);
  }
}
