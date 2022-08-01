package de.local.energycharts.testing.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

public class MastrRestAPIService {

  /**
   * To simulate the Marktstammdatenregister, as an external service with its own api,
   * we configure an empty wiremock server here (see also https://wiremock.org/).
   * In the docker-compose in this project, you can find the set-up to start the wiremock server.
   *
   * @param postcode The postcode to which the Marktstammdatenregister should respond with the following response.
   * @param solarSystemJsonResponse The response from the Marktstammdatenregister to simulate.
   */
  public void stubGetSolarSystems(int postcode, String solarSystemJsonResponse) {
    configureFor("localhost", 8082);
    stubFor(get(urlPathMatching(
        "/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/"
    ))
        .withQueryParam("page", equalTo("1"))
        .withQueryParam("pageSize", equalTo("1000000"))
        .withQueryParam("filter", equalTo("Postleitzahl~eq~'" + postcode + "'"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(solarSystemJsonResponse)
        )
    );
  }
}
