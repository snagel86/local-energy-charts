package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.port.MastrGateway;
import de.local.energycharts.solarcity.port.OpendatasoftGateway;
import de.local.energycharts.solarcity.port.SolarCityRepository;
import de.local.energycharts.solarcity.usecase.CreateOrUpdateSolarCity;
import de.local.energycharts.solarcity.usecase.GetAllPostcodes;
import de.local.energycharts.solarcity.usecase.GetAllSolarCities;
import de.local.energycharts.solarcity.usecase.UpdateAllSolarCities;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class SolarCityService implements
    CreateOrUpdateSolarCity,
    GetAllPostcodes,
    UpdateAllSolarCities,
    GetAllSolarCities {

  private final MastrGateway mastrGateway;
  private final OpendatasoftGateway opendatasoftGateway;
  private final SolarCityCacheService solarCityCacheService;
  private final SolarCityRepository solarCityRepository;
  private final Logger logger = LoggerFactory.getLogger(SolarCityService.class.getName());

  public Mono<SolarCity> createOrUpdateSolarCity(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityRepository.findByName(name)
        .defaultIfEmpty(SolarCity.createNewSolarCity(name))
        .map(solarCity -> solarCity
            .setUpdated(now())
            .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
            .setTargetYear(targetYear)
        )
        .flatMap(solarCity -> opendatasoftGateway.getPostcodes(name)
            .flatMap(mastrGateway::getSolarSystemsByPostcode)
            .collect(toSet())
            .map(solarCity::setSolarSystems)
            .flatMap(solarCityRepository::save)
        )
        .flatMap(solarCityCacheService::reset);
  }

  public Mono<SolarCity> createOrUpdateSolarCity(
      String name,
      String municipalityKey,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityRepository.findByName(name)
        .defaultIfEmpty(SolarCity.createNewSolarCity(name, municipalityKey))
        .map(solarCity -> solarCity
            .setUpdated(now())
            .setMunicipalityKey(municipalityKey)
            .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
            .setTargetYear(targetYear)
        )
        .flatMap(solarCity -> mastrGateway.getSolarSystemsByMunicipalityKey(municipalityKey)
            .collect(toSet())
            .map(solarCity::setSolarSystems)
            .flatMap(solarCityRepository::save)
        ).flatMap(solarCityCacheService::reset);
  }

  public Mono<SolarCity> createSolarCityTemporary(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    if (solarCityCacheService.isAlreadyCached(name)) {
      return solarCityCacheService
          .getByName(name)
          .map(solarCity -> solarCity
              .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
              .setTargetYear(targetYear)
          );
    }

    return Mono.just(SolarCity.createNewSolarCity(name)
        .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
        .setTargetYear(targetYear)
    ).flatMap(solarCity -> opendatasoftGateway.getPostcodes(name)
        .flatMap(mastrGateway::getSolarSystemsByPostcode)
        .collect(toSet())
        .map(solarCity::setSolarSystems)
        .flatMap(solarCityCacheService::cacheByName)
    );
  }

  public Mono<SolarCity> updateSolarCity(SolarCity solarCity) {
    solarCity.setUpdated(now());

    if (solarCity.getMunicipalityKey() != null) {
      return mastrGateway.getSolarSystemsByMunicipalityKey(solarCity.getMunicipalityKey())
          .collect(toSet())
          .map(solarCity::setSolarSystems)
          .flatMap(solarCityRepository::save)
          .flatMap(solarCityCacheService::reset);
    }

    return opendatasoftGateway.getPostcodes(solarCity.getName())
        .flatMap(mastrGateway::getSolarSystemsByPostcode)
        .collect(toSet())
        .map(solarCity::setSolarSystems)
        .flatMap(solarCityRepository::save)
        .flatMap(solarCityCacheService::reset);
  }

  public Flux<Integer> getAllPostcodes(String id) {
    return solarCityRepository
        .findById(id)
        .flatMapIterable(SolarCity::getAllPostcodes);
  }

  public Flux<SolarCity> updateAllSolarCities() {
    return solarCityRepository.findAll()
        .flatMap(this::updateSolarCity)
        .onErrorContinue((err, i) -> logger.error(err.getMessage()));
  }

  public Flux<SolarCity> getAllSolarCities() {
    return solarCityRepository.findAll();
  }
}
