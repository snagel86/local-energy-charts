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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolarCityUpdateServiceTest {

  @InjectMocks
  private SolarCityUpdateService solarCityUpdateService;
  @Mock
  private SolarCityRepository solarCityRepository;
  @Mock
  private SolarCityService solarCityService;

  @Test
  void update_all_solar_cities() {
    var koeln = SolarCity.createNewSolarCity("Köln").setId("1");
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("4");
    when(solarCityRepository.findAll())
        .thenReturn(Flux.just(koeln, frankfurt));
    when(solarCityService.updateSolarCity(koeln))
        .thenReturn(Mono.error(new IllegalStateException()));
    when(solarCityService.updateSolarCity(frankfurt))
        .thenReturn(Mono.just(frankfurt));

    assertThat(
        solarCityUpdateService.updateAllSolarCities()
            .map(SolarCity::getName)
            .collectList().block()
    ).containsExactly("Frankfurt");

    when(solarCityService.updateSolarCity(koeln))
        .thenReturn(Mono.just(koeln));

    assertThat(
        solarCityUpdateService.updateAllSolarCities()
            .map(SolarCity::getName)
            .collectList().block()
    ).containsExactlyInAnyOrder("Frankfurt", "Köln");
  }
}