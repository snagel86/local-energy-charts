package de.local.energycharts.solarcity.usecase;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.solarcity.ports.out.MastrGateway;
import de.local.energycharts.solarcity.ports.out.OpendatasoftGateway;
import de.local.energycharts.solarcity.ports.out.SolarCityCache;
import de.local.energycharts.solarcity.ports.out.SolarCityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static de.local.energycharts.solarcity.model.SolarCity.createNewSolarCity;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministrationTest {

  private Administration solarCityAdministration;
  @Mock
  private SolarCityRepository solarCityRepository;
  @Mock
  private MastrGateway mastrGateway;
  @Mock
  private OpendatasoftGateway opendatasoftGateway;
  @Mock
  private SolarCityCache solarCityCache;

  @BeforeEach
  void createSolarCityService() {
    solarCityAdministration = new Administration(
        mastrGateway,
        opendatasoftGateway,
        solarCityCache,
        solarCityRepository
    );
  }

  @Test
  void create_a_new_solar_city() {
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
        .thenAnswer(invocation -> (Mono.just(
                ((SolarCity) invocation.getArguments()[0])
                    .setId("1") // save returns the solar city with a new generated id;
            ))
        );
    when(opendatasoftGateway.getPostcodes("Frankfurt"))
        .thenReturn(Flux.fromIterable(List.of(60314, 60528)));
    when(solarCityCache.reset(any(SolarCity.class)))
        .thenAnswer(invocation -> (Mono.just(((SolarCity) invocation.getArguments()[0]))));

    SolarCity solarCity = solarCityAdministration.createOrUpdate(
        "Frankfurt",
        null, null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getAllSolarSystems()).hasSize(3);
  }

  @Test
  void create_or_update_an_existing_solar_city() {
    SolarCity frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

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
    when(solarCityCache.reset(any(SolarCity.class)))
        .thenAnswer(invocation -> (Mono.just(((SolarCity) invocation.getArguments()[0]))));

    SolarCity solarCity = solarCityAdministration.createOrUpdate(
        "Frankfurt",
        null, null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getAllSolarSystems()).hasSize(3);
  }

  @Test
  void create_or_update_an_existing_solar_city_by_municipality_key() {
    SolarCity frankfurt = SolarCity.createNewSolarCity("Frankfurt").setId("1");

    when(solarCityRepository.findByName("Frankfurt"))
        .thenReturn(Mono.just(frankfurt));
    when(mastrGateway.getSolarSystemsByMunicipalityKey("06412000", null))
        .thenReturn(Flux.fromIterable(List.of(
            SolarSystem.builder().id("1").status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").status(IN_OPERATION).build()
        )));
    when(solarCityRepository.save(any(SolarCity.class)))
        .thenAnswer(invocation -> Mono.just(invocation.getArguments()[0]));
    when(solarCityCache.reset(any(SolarCity.class)))
        .thenAnswer(invocation -> (Mono.just(((SolarCity) invocation.getArguments()[0]))));

    SolarCity solarCity = solarCityAdministration.createOrUpdate(
        "Frankfurt", "06412000",
        2100.0, 2035
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getAllSolarSystems()).hasSize(2);
  }

  @Test
  void update_an_existing_solar_city_by_municipality_key() {
    when(mastrGateway.getSolarSystemsByMunicipalityKey(
        "06412000",
        LocalDate.now().minusDays(3))
    ).thenReturn(Flux.fromIterable(List.of(
        SolarSystem.builder().id("1").status(IN_OPERATION).build(),
        SolarSystem.builder().id("2").status(IN_OPERATION).build()
    )));
    when(solarCityRepository.save(any(SolarCity.class)))
        .thenAnswer(invocation -> Mono.just(invocation.getArguments()[0]));
    when(solarCityCache.reset(any(SolarCity.class)))
        .thenAnswer(invocation -> (Mono.just(((SolarCity) invocation.getArguments()[0]))));

    SolarCity solarCity = solarCityAdministration.updateSolarCity(
        createNewSolarCity("Frankfurt", "06412000").setId("1")
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getAllSolarSystems()).hasSize(2);
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
    when(solarCityCache.isAlreadyCached("Frankfurt"))
        .thenReturn(false);
    when(solarCityCache.cacheByName(any(SolarCity.class)))
        .thenAnswer(invocation -> (Mono.just(((SolarCity) invocation.getArguments()[0]))));

    SolarCity solarCity = solarCityAdministration.createTemporary(
        "Frankfurt",
        null, null
    ).block();

    assertThat(solarCity.getName()).isEqualTo("Frankfurt");
    assertThat(solarCity.getAllSolarSystems()).hasSize(3);
  }

  @Test
  void get_all_postcodes() {
    var frankfurt = createNewSolarCity("Frankfurt")
        .setSolarSystems(Set.of(
            SolarSystem.builder().id("1").postcode(60314).status(IN_OPERATION).build(),
            SolarSystem.builder().id("2").postcode(60314).status(IN_OPERATION).build(),
            SolarSystem.builder().id("3").postcode(60528).status(IN_OPERATION).build()
        ));

    when(solarCityRepository.findById("Frankfurt"))
        .thenReturn(Mono.just(frankfurt));

    assertThat(solarCityAdministration.getAllPostcodes("Frankfurt").collectList().block())
        .containsExactly(60314, 60528);
  }

  @Test
  void update_all_solar_cities() {
    var solarCityService = Mockito.spy(new Administration(
        mastrGateway,
        opendatasoftGateway,
        solarCityCache,
        solarCityRepository
    ));
    var koeln = SolarCity.createNewSolarCity("Köln", "05315000").setId("1");
    var frankfurt = SolarCity.createNewSolarCity("Frankfurt", "06412000").setId("4");
    when(solarCityRepository.findAll())
        .thenReturn(Flux.just(koeln, frankfurt));
    doReturn(Mono.error(new IllegalStateException()))
        .when(solarCityService).updateSolarCity(koeln);
    doReturn(Mono.just(frankfurt))
        .when(solarCityService).updateSolarCity(frankfurt);

    assertThat(
        solarCityService.updateAll()
            .map(SolarCity::getName)
            .collectList().block()
    ).containsExactly("Frankfurt");

    when(solarCityService.updateSolarCity(koeln))
        .thenReturn(Mono.just(koeln));

    assertThat(
        solarCityService.updateAll()
            .map(SolarCity::getName)
            .collectList().block()
    ).containsExactlyInAnyOrder("Frankfurt", "Köln");
  }
}