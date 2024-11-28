package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.SolarCityCache;
import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.ports.out.SolarCityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolarCityCacheTest {

  @InjectMocks
  private SolarCityCache solarCityCache;
  @Mock
  private SolarCityRepository solarCityRepository;

  @Test
  void get_cached_solar_city() {
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

    when(solarCityRepository.findByIdSync("1"))
        .thenReturn(frankfurt);

    solarCityCache.get("1").block(); // find once from repository
    solarCityCache.get("1").block(); // then get from cache

    verify(solarCityRepository, atMostOnce()).findById("1");
  }

  @Test
  void get_null_when_cache_empty() {
    assertThat(solarCityCache.get("any").block()).isNull();
  }

  @Test
  void reset_cached_solar_city() {
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

    when(solarCityRepository.findByIdSync("1"))
        .thenReturn(frankfurt);

    solarCityCache.get("1").block();
    solarCityCache.reset(frankfurt).block();
    solarCityCache.get("1").block();

    verify(solarCityRepository, times(2)).findByIdSync("1");
  }

  @Test
  void cache_temporary_by_name() {
    var berlin = SolarCity.createNewSolarCity("Berlin");

    assertThat(solarCityCache.isAlreadyCached("Berlin")).isFalse();
    solarCityCache.cacheByName(berlin).subscribe();
    assertThat(solarCityCache.isAlreadyCached("Berlin")).isTrue();
    assertThat(solarCityCache.getByName("Berlin").block())
        .isEqualTo(berlin);
  }
}