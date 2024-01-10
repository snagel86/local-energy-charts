package de.local.energycharts.testing.service;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class OpendatasoftAPIService {

  /**
   * To simulate <a href="https://public.opendatasoft.com/explore/dataset/georef-germany-postleitzahl/information/">Opendatasoft</a>, as an external service with its own api,
   * we configure an empty wiremock server here (see also <a href="https://wiremock.org/">https://wiremock.org/</a>).
   * In the docker-compose in this project, you can find the set-up to start the wiremock server.
   *
   * @param cityName The name of the city to which Opendatasoft should respond with the following post codes.
   * @param response The response from the Opendatasoft to simulate.
   */
  public void stubGetPostcodes(String cityName, String response) {
    configureFor("localhost", 8083);
    stubFor(get(urlPathMatching(
        "/api/records/1.0/search/"
    ))
        .withQueryParam("dataset", equalTo("georef-germany-postleitzahl"))
        .withQueryParam("rows", equalTo("10000"))
        .withQueryParam("refine.plz_name", equalTo(cityName))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(response)
        )
    );
  }
}
