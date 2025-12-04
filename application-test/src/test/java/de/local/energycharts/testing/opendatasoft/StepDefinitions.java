package de.local.energycharts.testing.opendatasoft;

import io.cucumber.java.en.Given;

import java.util.List;

public class StepDefinitions {

  private final WireMockServer wireMockServer = new WireMockServer();

  @Given("(is, )that for the postcode search for {string}, the following postcodes are stored in Opendatasoft")
  public void stubTheGivenResponse(String cityName, List<Integer> postcodes) {
    var response = new PostcodeResponseBuilder()
        .withPostcodes(postcodes)
        .build();
    wireMockServer.stubGetPostcodes(cityName, response);
  }
}
