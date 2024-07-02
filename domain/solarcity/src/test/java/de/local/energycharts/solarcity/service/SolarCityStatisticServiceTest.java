package de.local.energycharts.solarcity.service;

import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class SolarCityStatisticServiceTest {

  @InjectMocks
  private SolarCityStatisticService solarCityStatisticService;
  @Mock
  private SolarCityRepository solarCityRepository;

  @Test
  void get_cached_solar_city() {
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

    when(solarCityRepository.findByName("Frankfurt"))
        .thenReturn(Mono.just(frankfurt));

    solarCityStatisticService.getSolarCity("Frankfurt").block(); // find once from repository
    solarCityStatisticService.getSolarCity("Frankfurt").block(); // then get from cache

    verify(solarCityRepository, atMostOnce()).findByName("Frankfurt");
  }

  @Test
  void reset_cached_solar_city() {
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

    when(solarCityRepository.findByName("Frankfurt"))
        .thenReturn(Mono.just(frankfurt));

    solarCityStatisticService.getSolarCity("Frankfurt").block();
    solarCityStatisticService.resetCachedSolarCity("Frankfurt").block();
    solarCityStatisticService.getSolarCity("Frankfurt").block();

    verify(solarCityRepository, times(2)).findByName("Frankfurt");
  }
}