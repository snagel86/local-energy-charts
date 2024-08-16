package de.local.energycharts.testing.mastr;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.json.JSONObject;

import java.util.List;

public class Step {

  private final WireMockServer wireMockServer = new WireMockServer();
  private final SolarResponseBuilder solarResponseBuilder = new SolarResponseBuilder();
  private Integer postcode;

  @Given("(is, )that for the Gemeindeschlüssel {string}, the following solar systems are registered in the Marktstammdatenregister")
  public void stubSolarResponse(String municipalityKey, DataTable givenSolarSystems) {
    List<JSONObject> responses = solarResponseBuilder
        .from(givenSolarSystems)
        .build();
    int page = 1;
    for (JSONObject response : responses) {
      wireMockServer.stubGetSolarSystems(municipalityKey, response, page++);
    }
  }

  @Given("(is, )that for the postcode {int}, the following solar systems are registered in the Marktstammdatenregister")
  public void stubSolarResponse(int postcode, DataTable givenSolarSystems) {
    List<JSONObject> responses = solarResponseBuilder
        .from(givenSolarSystems)
        .build();
    int page = 1;
    for (JSONObject response : responses) {
      wireMockServer.stubGetSolarSystems(postcode, response, page++);
    }
  }

  @Given("that in {int} for postcode {int}, {int} Balkonkraftwerke \\(with 0.6 kWp)")
  public void addBalkonkraftwerke(int year, int postcode, int count) {
    solarResponseBuilder
        .withYear(year)
        .withPostcode(postcode)
        .addBalkonkraftwerksWith06kWp(count);
    this.postcode = postcode;
  }

  @Given("that in {int} for postcode {int}, {int} homes \\(with 5.0 kWp)")
  public void addHomes(int year, int postcode, int count) {
    solarResponseBuilder
        .withYear(year)
        .withPostcode(postcode)
        .addHomesWith5kWp(count);
    this.postcode = postcode;
  }

  @And("{int} solar systems on homes \\(with 5.0 kWp)")
  public void addHomes(int count) {
    solarResponseBuilder.addHomesWith5kWp(count);
  }

  @And("{int} solar systems on apartment buildings \\(with 25.0 kWp)")
  public void addApartmentBuildings(int count) {
    solarResponseBuilder
        .addApartmentBuildingsWith25kWp(count);
  }

  @And("{int} solar systems on commercial buildings \\(with 100.0 kWp)")
  public void addCommercialBuildings(int count) {
    solarResponseBuilder
        .addCommercialBuildingsWith100kWp(count);
  }

  @And("{int} on schools \\(with 100.0 kWp) are registered in the Marktstammdatenregister")
  public void addSchoolsAndStubSolarResponse(int count) {
    List<JSONObject> responses = solarResponseBuilder
        .addSchoolsWith100kWp(count)
        .build();
    int page = 1;
    for (JSONObject response : responses) {
      wireMockServer.stubGetSolarSystems(postcode, response, page++);
    }
  }
}
