package de.local.energycharts.mastr.service;

import static de.local.energycharts.core.model.SolarSystem.Status.IN_OPERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import de.local.energycharts.core.model.SolarCity;
import de.local.energycharts.core.model.SolarSystem;
import de.local.energycharts.core.repository.SolarCityRepository;
import de.local.energycharts.mastr.gateway.MastrGateway;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class MastrSolarServiceTest {

  @InjectMocks
  private MastrSolarService mastrSolarService;
  @Mock
  private SolarCityRepository solarCityRepository;
  @Mock
  private MastrGateway mastrGateway;

  @Test
  void create_a_solar_city() {
    when(solarCityRepository.findByName("Frankfurt"))
        .thenReturn(Mono.empty());
    when(mastrGateway.getSolarSystems(60314))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(IN_OPERATION).build()
        )));
    when(mastrGateway.getSolarSystems(60528))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("3").status(IN_OPERATION).build()
        )));
    when(solarCityRepository.save(any(SolarCity.class)))
        .thenAnswer(invocation -> Mono.just(invocation.getArguments()[0]));

    SolarCity solarCity = mastrSolarService.createOrUpdateSolarCity(
        "Frankfurt",
        Set.of(60314, 60528),
        null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getDistricts()).hasSize(2);
    assertThat(solarCity.calculateTotalNumberOfSolarInstallations()).isEqualTo(3);
  }

  @Test
  void update_a_solar_city() {
    SolarCity frankfurt = SolarCity.createNewSolarCity("Frankfurt");

    when(solarCityRepository.findByName("Frankfurt"))
        .thenReturn(Mono.just(frankfurt));
    when(mastrGateway.getSolarSystems(60314))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(IN_OPERATION).build()
        )));
    when(mastrGateway.getSolarSystems(60528))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("3").status(IN_OPERATION).build()
        )));
    when(solarCityRepository.save(any(SolarCity.class)))
        .thenAnswer(invocation -> Mono.just(invocation.getArguments()[0]));

    SolarCity solarCity = mastrSolarService.createOrUpdateSolarCity(
        "Frankfurt",
        Set.of(60314, 60528),
        null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getDistricts()).hasSize(2);
    assertThat(solarCity.calculateTotalNumberOfSolarInstallations()).isEqualTo(3);
  }

  @Test
  void create_a_solar_city_temporary() {
    when(mastrGateway.getSolarSystems(60314))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(IN_OPERATION).build()
        )));
    when(mastrGateway.getSolarSystems(60528))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("3").status(IN_OPERATION).build()
        )));

    SolarCity solarCity = mastrSolarService.createSolarCityTemporary(
        "Frankfurt",
        Set.of(60314, 60528),
        null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getDistricts()).hasSize(2);
    assertThat(solarCity.calculateTotalNumberOfSolarInstallations()).isEqualTo(3);
  }
}