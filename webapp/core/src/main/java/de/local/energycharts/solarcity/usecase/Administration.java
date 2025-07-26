package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.ports.in.AdministrateSolarCity;
import de.local.energycharts.solarcity.ports.out.MastrGateway;
import de.local.energycharts.solarcity.ports.out.OpendatasoftGateway;
import de.local.energycharts.solarcity.ports.out.SolarCityCache;
import de.local.energycharts.solarcity.ports.out.SolarCityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class Administration implements AdministrateSolarCity {

  private final MastrGateway mastrGateway;
  private final OpendatasoftGateway opendatasoftGateway;
  private final SolarCityCache solarCityCache;
  private final SolarCityRepository solarCityRepository;
  private final Logger logger = LoggerFactory.getLogger(Administration.class.getName());

  public Mono<SolarCity> createOrUpdate(
      String name,
      String municipalityKey,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    if (municipalityKey != null) {
      return createOrUpdateByMunicipalityKey(name, municipalityKey, entireSolarPotentialOnRooftopsMWp, targetYear);
    }
    return createOrUpdateByPostcodes(name, entireSolarPotentialOnRooftopsMWp, targetYear);
  }

  private Mono<SolarCity> createOrUpdateByMunicipalityKey(
      String name,
      String municipalityKey,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityRepository.findByName(name)
        .defaultIfEmpty(SolarCity.createNewSolarCity(name, municipalityKey))
        .flatMap(solarCity -> mastrGateway.getSolarSystemsByMunicipalityKey(
                    municipalityKey,
                    createLastUpdateIfUpdateCase(solarCity)
                ).collect(toSet())
                .map(solarCity::addSolarSystems)
        )
        // update params
        .map(solarCity -> solarCity
            .setUpdated(now())
            .setMunicipalityKey(municipalityKey)
            .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
            .setTargetYear(targetYear)
        ).flatMap(solarCityRepository::save)
        .flatMap(solarCityCache::reset);
  }

  private LocalDate createLastUpdateIfUpdateCase(SolarCity solarCity) {
    return solarCity.getUpdated() != null ? LocalDate.now().minusDays(1) : null;
  }

  private Mono<SolarCity> createOrUpdateByPostcodes(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityRepository.findByName(name)
        .defaultIfEmpty(SolarCity.createNewSolarCity(name))
        .flatMap(solarCity -> opendatasoftGateway.getPostcodes(name)
            .flatMap(mastrGateway::getSolarSystemsByPostcode)
            .collect(toSet())
            .map(solarCity::addSolarSystems)
        )
        // update params
        .map(solarCity -> solarCity
            .setUpdated(now())
            .setEntireSolarPotentialOnRooftopsMWp(entireSolarPotentialOnRooftopsMWp)
            .setTargetYear(targetYear)
        ).flatMap(solarCityRepository::save)
        .flatMap(solarCityCache::reset);
  }

  public Mono<SolarCity> createTemporary(
      String name,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    if (solarCityCache.isAlreadyCached(name)) {
      return solarCityCache
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
        .map(solarCity::addSolarSystems)
        .flatMap(solarCityCache::cacheByName)
    );
  }

  public Mono<SolarCity> updateSolarCity(SolarCity solarCity) {
    solarCity.setUpdated(now());

    if (solarCity.getMunicipalityKey() != null) {
      return updateByMunicipalityKey(solarCity);
    }
    return updateByPostcodes(solarCity);
  }

  private Mono<SolarCity> updateByMunicipalityKey(SolarCity solarCity) {
    return mastrGateway.getSolarSystemsByMunicipalityKey(
            solarCity.getMunicipalityKey(),
            createLastUpdateIfUpdateCase(solarCity)
        ).collect(toSet())
        .map(solarCity::addSolarSystems)
        .flatMap(solarCityRepository::save)
        .flatMap(solarCityCache::reset);
  }

  private Mono<SolarCity> updateByPostcodes(SolarCity solarCity) {
    return opendatasoftGateway.getPostcodes(solarCity.getName())
        .flatMap(mastrGateway::getSolarSystemsByPostcode)
        .collect(toSet())
        .map(solarCity::addSolarSystems)
        .flatMap(solarCityRepository::save)
        .flatMap(solarCityCache::reset);
  }

  public Flux<Integer> getAllPostcodes(String id) {
    return solarCityRepository
        .findById(id)
        .flatMapIterable(SolarCity::getAllPostcodes);
  }

  public Flux<SolarCity> updateAll() {
    return solarCityRepository.findAll()
        .flatMap(this::updateSolarCity)
        .onErrorContinue((err, i) -> logger.error(err.getMessage()));
  }

  public Flux<SolarCity> getAll() {
    return solarCityRepository.findAll();
  }

  public void delete(String id) {
    solarCityRepository.deleteById(id);
  }
}
