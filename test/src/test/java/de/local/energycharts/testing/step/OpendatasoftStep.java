package de.local.energycharts.testing.step;

import de.local.energycharts.testing.builder.OpendatasoftPlzResponseBuilder;
import de.local.energycharts.testing.server.OpendatasoftWireMockServer;
import io.cucumber.java.en.Given;

import java.util.List;

public class OpendatasoftStep {

  private final OpendatasoftWireMockServer opendatasoftWireMockServer = new OpendatasoftWireMockServer();

  @Given("(is, )that for the postcode search for {string}, the following postcodes are stored in Opendatasoft")
  public void stubTheGivenResponse(String cityName, List<Integer> postcodes) {
    var response = new OpendatasoftPlzResponseBuilder()
        .withPostcodes(postcodes)
        .build();
    opendatasoftWireMockServer.stubGetPostcodes(cityName, response);
  }
}
