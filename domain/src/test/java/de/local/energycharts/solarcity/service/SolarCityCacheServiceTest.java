package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolarCityCacheServiceTest {

  @InjectMocks
  private SolarCityCacheService solarCityCacheService;
  @Mock
  private SolarCityRepository solarCityRepository;

  @Test
  void get_cached_solar_city() {
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

    when(solarCityRepository.findByIdSync("1"))
        .thenReturn(frankfurt);

    solarCityCacheService.get("1").block(); // find once from repository
    solarCityCacheService.get("1").block(); // then get from cache

    verify(solarCityRepository, atMostOnce()).findById("1");
  }

  @Test
  void get_null_when_cache_empty() {
    assertThat(solarCityCacheService.get("any").block()).isNull();
  }

  @Test
  void reset_cached_solar_city() {
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

    when(solarCityRepository.findByIdSync("1"))
        .thenReturn(frankfurt);

    solarCityCacheService.get("1").block();
    solarCityCacheService.reset(frankfurt).block();
    solarCityCacheService.get("1").block();

    verify(solarCityRepository, times(2)).findByIdSync("1");
  }

  @Test
  void cache_temporary_by_name() {
    var berlin = SolarCity.createNewSolarCity("Berlin");

    assertThat(solarCityCacheService.isAlreadyCached("Berlin")).isFalse();
    solarCityCacheService.cacheByName(berlin).subscribe();
    assertThat(solarCityCacheService.isAlreadyCached("Berlin")).isTrue();
    assertThat(solarCityCacheService.getByName("Berlin").block())
        .isEqualTo(berlin);
  }
}