package de.local.energycharts.core.service;

import static java.time.LocalDate.now;
import static java.util.Comparator.comparing;
import static java.util.concurrent.TimeUnit.HOURS;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.local.energycharts.core.model.SolarCity;
import de.local.energycharts.core.model.statistic.AdditionOfSolarInstallations;
import de.local.energycharts.core.model.statistic.OperatorOverview;
import de.local.energycharts.core.model.statistic.SolarCityOverview;
import de.local.energycharts.core.repository.SolarCityRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SolarCityStatisticService {

  private final SolarCityRepository solarCityRepository;
  private final Cache<String, Mono<SolarCity>> solarCityCache;

  public SolarCityStatisticService(SolarCityRepository solarCityRepository) {

    this.solarCityRepository = solarCityRepository;

    solarCityCache = CacheBuilder.newBuilder()
        .expireAfterWrite(24, HOURS)
        .concurrencyLevel(Runtime.getRuntime().availableProcessors())
        .build();
  }

  public Mono<SolarCityOverview> createSolarCityOverview(String city) {
    return solarCityRepository.findByIdOrName(city, city)
        .map(SolarCity::calculateSolarCityOverview);
  }

  public Flux<OperatorOverview> createOperatorOverviews(String city) {
    return solarCityRepository.findByIdOrName(city, city)
        .flatMapIterable(SolarCity::calculateOperatorOverview)
        .sort(
            comparing(OperatorOverview::getNumberOfSolarSystems).reversed()
                .thenComparing(OperatorOverview::getOperatorName)
        );
  }

  public Mono<SolarCity> getSolarCity(String city) {
    return getCachedSolarCity(city);
  }

  public Flux<AdditionOfSolarInstallations> createAnnualAdditionOfSolarInstallations(
      String city,
      boolean previousSolarInstallationsOnly
  ) {
    return getCachedSolarCity(city)
        .flatMapIterable(SolarCity::calculateAnnualAdditionOfSolarInstallations)
        .filter(addition -> !previousSolarInstallationsOnly || addition.getYear() <= now().getYear());
  }

  @SneakyThrows
  private Mono<SolarCity> getCachedSolarCity(String city) {
    return solarCityCache.get(city, () -> solarCityRepository.findByIdOrName(city, city));
  }

  public Mono<SolarCity> resetCachedSolarCity(String city) {
    solarCityCache.invalidate(city);
    return getCachedSolarCity(city);
  }
}
