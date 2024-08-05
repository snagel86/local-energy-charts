package de.local.energycharts.testing.step;

import de.local.energycharts.testing.service.OpendatasoftAPIService;
import de.local.energycharts.testing.step.builder.OpendatasoftPlzResponseBuilder;
import io.cucumber.java.en.Given;

import java.util.List;

public class OpendatasoftStep {

  private final OpendatasoftAPIService opendatasoftAPIService = new OpendatasoftAPIService();

  @Given("(is, )that for the postcode search for {string}, the following postcodes are stored in Opendatasoft")
  public void stubTheGivenResponse(String cityName, List<Integer> postcodes) {
    var response = new OpendatasoftPlzResponseBuilder(postcodes).build();
    opendatasoftAPIService.stubGetPostcodes(cityName, response.toString());
  }
}
