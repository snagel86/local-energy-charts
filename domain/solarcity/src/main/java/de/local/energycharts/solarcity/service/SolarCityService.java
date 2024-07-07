package de.local.energycharts.solarcity.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.local.energycharts.solarcity.gateway.MastrGateway;
import de.local.energycharts.solarcity.gateway.OpendatasoftGateway;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.stream.Collectors.toSet;

@Service
public class SolarCityService {

  private final MastrGateway mastrGateway;
  private final OpendatasoftGateway opendatasoftGateway;
  private final SolarCityRepository solarCityRepository;
  private final Cache<String, SolarCity> solarCityCache;

  public SolarCityService(
      MastrGateway mastrGateway,
      OpendatasoftGateway opendatasoftGateway,
      SolarCityRepository solarCityRepository
  ) {
    this.mastrGateway = mastrGateway;
    this.opendatasoftGateway = opendatasoftGateway;
    this.solarCityRepository = solarCityRepository;
    solarCityCache = CacheBuilder.newBuilder()
        .expireAfterWrite(24, HOURS)
        .concurrencyLevel(Runtime.getRuntime().availableProcessors())
        .build();
  }

  @SneakyThrows
  public Mono<SolarCity> getCachedSolarCity(String city) {
    return Mono.just(
        solarCityCache.get(city, () -> solarCityRepository.findByNameSync(city))
    );
  }

  Mono<SolarCity> resetCachedSolarCity(SolarCity solarCity) {
    solarCityCache.invalidate(solarCity.getName());
    return Mono.just(solarCity);
  }

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
            .flatMap(mastrGateway::getSolarSystemsByPostcode)
            .collect(toSet())
            .map(solarCity::setSolarSystems)
            .flatMap(solarCityRepository::save)
        )
        .flatMap(this::resetCachedSolarCity);
  }

  public Mono<SolarCity> createOrUpdateSolarCity(
      String cityName,
      String municipalityKey,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    return solarCityRepository.findByName(cityName)
        .defaultIfEmpty(SolarCity.createNewSolarCity(cityName, municipalityKey))
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
        ).flatMap(this::resetCachedSolarCity);
  }

  public Mono<SolarCity> updateSolarCity(SolarCity solarCity) {
    solarCity.setUpdated(now());

    if (solarCity.getMunicipalityKey() != null) {
      return mastrGateway.getSolarSystemsByMunicipalityKey(solarCity.getMunicipalityKey())
          .collect(toSet())
          .map(solarCity::setSolarSystems)
          .flatMap(solarCityRepository::save)
          .flatMap(this::resetCachedSolarCity);
    }

    return opendatasoftGateway.getPostcodes(solarCity.getName())
        .flatMap(mastrGateway::getSolarSystemsByPostcode)
        .collect(toSet())
        .map(solarCity::setSolarSystems)
        .flatMap(solarCityRepository::save)
        .flatMap(this::resetCachedSolarCity);
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
            .flatMap(mastrGateway::getSolarSystemsByPostcode)
            .collect(toSet())
            .map(solarCity::setSolarSystems)
        );
  }

  public Flux<SolarCity> getAllSolarCities() {
    return solarCityRepository.findAll();
  }

  public Flux<Integer> getAllPostcodes(String city) {
    return solarCityRepository
        .findByName(city)
        .flatMapIterable(SolarCity::getAllPostcodes);
  }
}
