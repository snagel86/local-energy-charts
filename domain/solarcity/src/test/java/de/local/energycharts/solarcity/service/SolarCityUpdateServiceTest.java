package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolarCityUpdateServiceTest {

  @InjectMocks
  private SolarCityUpdateService solarCityUpdateService;
  @Mock
  private SolarCityRepository solarCityRepository;
  @Mock
  private SolarCityService solarCityService;
  @Mock
  private SolarCityStatisticService solarCityStatisticService;

  @Test
  void update_all_solar_cities() {
    var koeln = SolarCity.createNewSolarCity("Köln").setId("1");
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt am Main").setId("4");
    when(solarCityRepository.findAll())
        .thenReturn(Flux.just(koeln, frankfurt));
    when(solarCityService.updateSolarCity(koeln))
        .thenReturn(Mono.error(new IllegalStateException()));
    when(solarCityService.updateSolarCity(frankfurt))
        .thenReturn(Mono.just(frankfurt));
    when(solarCityStatisticService.resetCachedSolarCity(frankfurt.getName()))
        .thenReturn(Mono.just(frankfurt));

    solarCityUpdateService.updateAllSolarCities().subscribe();
    verify(solarCityStatisticService, never()).resetCachedSolarCity(koeln.getName());
    verify(solarCityStatisticService, times(1)).resetCachedSolarCity(frankfurt.getName());

    when(solarCityService.updateSolarCity(koeln))
        .thenReturn(Mono.just(koeln));

    solarCityUpdateService.updateAllSolarCities().subscribe();
    verify(solarCityStatisticService, times(1)).resetCachedSolarCity(koeln.getName());
    verify(solarCityStatisticService, times(1)).resetCachedSolarCity(frankfurt.getName());
  }
}