package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import de.local.energycharts.solarcity.gateway.MastrGateway;
import de.local.energycharts.solarcity.gateway.OpendatasoftGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolarCityServiceTest {

  @InjectMocks
  private SolarCityService solarCityService;
  @Mock
  private SolarCityRepository solarCityRepository;
  @Mock
  private MastrGateway mastrGateway;
  @Mock
  private OpendatasoftGateway opendatasoftGateway;

  @Test
  void create_a_solar_city() {
    when(solarCityRepository.findByName("Frankfurt"))
        .thenReturn(Mono.empty());
    when(mastrGateway.getSolarSystemsByPostcode(60314))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(IN_OPERATION).build()
        )));
    when(mastrGateway.getSolarSystemsByPostcode(60528))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("3").status(IN_OPERATION).build()
        )));
    when(solarCityRepository.save(any(SolarCity.class)))
        .thenAnswer(invocation -> Mono.just(invocation.getArguments()[0]));
    when(opendatasoftGateway.getPostcodes("Frankfurt"))
        .thenReturn(Flux.fromIterable(List.of(60314, 60528)));

    SolarCity solarCity = solarCityService.createOrUpdateSolarCity(
        "Frankfurt",
        null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.calculateTotalNumberOfSolarInstallations()).isEqualTo(3);
  }

  @Test
  void update_a_solar_city() {
    SolarCity frankfurt = SolarCity.createNewSolarCity("Frankfurt");

    when(solarCityRepository.findByName("Frankfurt"))
        .thenReturn(Mono.just(frankfurt));
    when(mastrGateway.getSolarSystemsByPostcode(60314))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(IN_OPERATION).build()
        )));
    when(mastrGateway.getSolarSystemsByPostcode(60528))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("3").status(IN_OPERATION).build()
        )));
    when(solarCityRepository.save(any(SolarCity.class)))
        .thenAnswer(invocation -> Mono.just(invocation.getArguments()[0]));
    when(opendatasoftGateway.getPostcodes("Frankfurt"))
        .thenReturn(Flux.fromIterable(List.of(60314, 60528)));

    SolarCity solarCity = solarCityService.createOrUpdateSolarCity(
        "Frankfurt",
        null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.calculateTotalNumberOfSolarInstallations()).isEqualTo(3);
  }

  @Test
  void create_a_solar_city_temporary() {
    when(mastrGateway.getSolarSystemsByPostcode(60314))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(IN_OPERATION).build()
        )));
    when(mastrGateway.getSolarSystemsByPostcode(60528))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("3").status(IN_OPERATION).build()
        )));
    when(opendatasoftGateway.getPostcodes("Frankfurt"))
        .thenReturn(Flux.fromIterable(List.of(60314, 60528)));

    SolarCity solarCity = solarCityService.createSolarCityTemporary(
        "Frankfurt",
        null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.calculateTotalNumberOfSolarInstallations()).isEqualTo(3);
  }

  @Test
  void get_all_postcodes() {
    var frankfurt = SolarCity
        .createNewSolarCity("Frankfurt")
        .setSolarSystems(Set.of(
            SolarSystem.builder().id("1").postcode(60314).status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").postcode(60314).status(IN_OPERATION).build(),
            SolarSystem.builder().id("3").postcode(60528).status(IN_OPERATION).build()
        ));

    when(solarCityRepository.findByIdOrName("Frankfurt", "Frankfurt"))
        .thenReturn(Mono.just(frankfurt));

    assertThat(solarCityService.getAllPostcodes("Frankfurt").collectList().block())
        .containsExactly(60314, 60528);
  }
}