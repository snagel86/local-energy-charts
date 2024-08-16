package de.local.energycharts.testing.mastr;

import io.cucumber.java.After;
import org.json.JSONObject;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static de.local.energycharts.testing.mastr.SolarResponseBuilder.PAGE_SIZE;

public class WireMockServer {

  private final static int PORT = 8082;

  /**
   * To simulate the Marktstammdatenregister, as an external service with its own api,
   * we configure an empty wiremock server here (see also <a href="https://wiremock.org/">https://wiremock.org/</a>).
   * In the docker-compose in this project, you can find the set-up to start the wiremock server.
   *
   * @param postcode The postcode to which the Marktstammdatenregister should respond with the following response.
   * @param response The response from the Marktstammdatenregister to simulate.
   */
  public void stubGetSolarSystems(int postcode, JSONObject response, Integer page) {
    configureFor("localhost", PORT);
    stubFor(get(urlPathMatching(
            "/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/"
        ))
            .withQueryParam("page", equalTo(page.toString()))
            .withQueryParam("pageSize", equalTo(PAGE_SIZE.toString()))
            .withQueryParam("filter", equalTo("Postleitzahl~eq~'" + postcode + "'~and~Energieträger~eq~'2495'"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())
            )
    );
  }

  public void stubGetSolarSystems(String municipalityKey, JSONObject response, Integer page) {
    configureFor("localhost", PORT);
    stubFor(get(urlPathMatching(
            "/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/"
        ))
            .withQueryParam("page", equalTo(page.toString()))
            .withQueryParam("pageSize", equalTo(PAGE_SIZE.toString()))
            .withQueryParam("filter", equalTo("Gemeindeschlüssel~eq~'" + municipalityKey + "'~and~Energieträger~eq~'2495'"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())
            )
    );
  }

  @After
  public void reset() {
    configureFor("localhost", PORT);
    resetAllRequests();
  }
}
