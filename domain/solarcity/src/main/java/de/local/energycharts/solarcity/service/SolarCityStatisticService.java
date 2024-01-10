package de.local.energycharts.solarcity.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import de.local.energycharts.solarcity.model.statistic.SolarBuildingPieChart;
import de.local.energycharts.solarcity.model.statistic.SolarCityOverview;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.concurrent.TimeUnit.HOURS;

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

  public Mono<SolarCityOverview> createOverview(String city) {
    return getCachedSolarCity(city).map(SolarCity::calculateSolarCityOverview);
  }

  public Mono<SolarBuildingPieChart> createSolarBuildingPieChart(String city) {
    return getCachedSolarCity(city).map(SolarCity::calculateSolarBuildingPieChart);
  }

  public Mono<MonthlySolarInstallations> createMonthlySolarInstallations(String city) {
    return getCachedSolarCity(city).map(SolarCity::calculateMonthlySolarInstallations);
  }

  public Mono<SolarCity> getSolarCity(String city) {
    return getCachedSolarCity(city);
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
