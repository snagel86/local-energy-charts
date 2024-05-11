package de.local.energycharts.infrastructure.mastr.gateway;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static de.local.energycharts.solarcity.model.SolarSystem.Status.IN_OPERATION;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.infrastructure.mastr.model.mapper.SolarSystemMapper;
import de.local.energycharts.solarcity.gateway.MastrGateway;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MastrRestApiClientGatewayTest {

  private WireMockServer wireMockServer;
  private MastrGateway mastrGateway;
  @Spy
  private SolarSystemMapper solarSystemMapper = Mappers.getMapper(SolarSystemMapper.class);

  private SolarSystem testSolarSystem;
  private final int testPostcode = 60314;

  @BeforeEach
  void setUp() {
    wireMockServer = new WireMockServer();
    wireMockServer.start();
    mastrGateway = new MastrRestApiGateway(
        solarSystemMapper,
        WebClient.create(wireMockServer.baseUrl())
    );
  }

  @BeforeEach
  void createTestSolarSystem() {
    testSolarSystem = SolarSystem.builder()
        .id("1")
        .commissioning(LocalDate.of(2010, 8, 20))
        .lastChange(Instant.parse("2019-05-28T08:26:38.031Z"))
        .installedGrossPowerkWp(236.41)
        .installedNetPowerkWp(221.0)
        .operatorName("Energiegenossenschaft Frankfurt")
        .name("Solaranlage")
        .status(IN_OPERATION).build();
  }

  @AfterEach
  void tearDown() {
    wireMockServer.stop();
  }

  @Test
  void get_solar_systems() {
    stubForGetSolarSystemsOK200();
    List<SolarSystem> result = mastrGateway.getSolarSystemsByPostcode(testPostcode)
        .collectList().block();

    assertThat(result).containsExactly(testSolarSystem);
  }

  @Test
  void get_solar_systems_failed_but_retry_successful() throws InterruptedException {
    stubForGetSolarSystemsInternalServerError500();
    Flux<SolarSystem> result = mastrGateway.getSolarSystemsByPostcode(testPostcode);

    sleep(2100L);
    wireMockServer.resetAll();
    stubForGetSolarSystemsOK200();

    assertThat(result.collectList().block()).containsExactly(testSolarSystem);
  }

  @Test
  void get_solar_systems_finally_failed() {
    stubForGetSolarSystemsInternalServerError500();
    Flux<SolarSystem> result = mastrGateway.getSolarSystemsByPostcode(testPostcode);

    StepVerifier.create(result)
        .expectError()
        .verify();
  }

  private void stubForGetSolarSystemsOK200() {
    StubMapping stubMapping = stubFor(get(urlPathMatching(
        "/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/"
    ))
        .withQueryParam("page", equalTo("1"))
        .withQueryParam("pageSize", equalTo("1000000"))
        .withQueryParam("filter", equalTo("Postleitzahl~eq~'" + testPostcode + "'~and~Energieträger~eq~'2495'"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("""
                {
                  "Data": [
                  {
                    "Id": 1,
                    "AnlagenbetreiberId": 1,
                    "AnlagenbetreiberMaStRNummer": "ABR123456789",
                    "AnlagenbetreiberName": "Energiegenossenschaft Frankfurt",
                    "BetriebsStatusId": 35,
                    "BetriebsStatusName": "In Betrieb",
                    "DatumLetzteAktualisierung": "/Date(1559031998031)/",
                    "EinheitMeldeDatum": "/Date(1548979200000)/",
                    "EinheitName": "Solaranlage",
                    "InbetriebnahmeDatum": "/Date(1282262400000)/",
                    "MaStRNummer": "SEE123456789",
                    "Ort": "Frankfurt",
                    "Plz": "60314",
                    "AnzahlSolarModule": 1,
                    "Bruttoleistung": 236.410,
                    "Nettonennleistung": 221.000,
                    "EnergietraegerName": "Solare Strahlungsenergie"
                  }
                 ]
                }
                """)
        ));
  }

  private void stubForGetSolarSystemsInternalServerError500() {
    stubFor(get(urlPathMatching(
        "/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/"
    ))
        .withQueryParam("page", equalTo("1"))
        .withQueryParam("pageSize", equalTo("1000000"))
        .withQueryParam("filter", equalTo("Postleitzahl~eq~'" + testPostcode + "'~and~Energieträger~eq~'2495'"))
        .willReturn(aResponse().withStatus(500)));
  }
}