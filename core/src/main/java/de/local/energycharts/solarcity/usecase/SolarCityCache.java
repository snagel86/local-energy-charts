package de.local.energycharts.solarcity.usecase;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.ports.out.SolarCityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.HOURS;

@Service
public class SolarCityCache {

  private final SolarCityRepository solarCityRepository;
  private final Cache<String, SolarCity> solarCityCache;

  public SolarCityCache(
      SolarCityRepository solarCityRepository
  ) {
    this.solarCityRepository = solarCityRepository;
    solarCityCache = CacheBuilder.newBuilder()
        .expireAfterWrite(24, HOURS)
        .concurrencyLevel(Runtime.getRuntime().availableProcessors())
        .build();
  }

  public Mono<SolarCity> get(String id) {
    try {
      return Mono.just(
          solarCityCache.get(id, () -> solarCityRepository.findByIdSync(id))
      );
    } catch (CacheLoader.InvalidCacheLoadException | ExecutionException e) {
      return Mono.empty();
    }
  }

  Mono<SolarCity> reset(SolarCity solarCity) {
    // remove
    solarCityCache.invalidate(solarCity.getId());
    // reload
    return get(solarCity.getId())
        .defaultIfEmpty(solarCity);
  }

  public Mono<SolarCity> cacheByName(SolarCity solarCity) {
    solarCityCache.put(solarCity.getName(), solarCity);
    return getByName(solarCity.getName());
  }

  public boolean isAlreadyCached(String name){
    return solarCityCache.getIfPresent(name) != null;
  }

  public Mono<SolarCity> getByName(String name) {
    var solarCity = solarCityCache.getIfPresent(name);
    if (solarCity != null) {
      return Mono.just(solarCity);
    }
    return Mono.empty();
  }
}
