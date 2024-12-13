package de.local.energycharts.testing.mastr;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import jakarta.json.JsonObject;

import java.util.List;

public class StepDefinitions {

  private final WireMockServer wireMockServer = new WireMockServer();
  private final SolarResponseBuilder solarResponseBuilder = new SolarResponseBuilder();
  private String municipalityKey;

  @Given("(is, )that for the Gemeindeschlüssel {string}, the following solar systems are registered in the Marktstammdatenregister")
  public void stubSolarResponse(String municipalityKey, DataTable givenSolarSystems) {
    List<JsonObject> responses = solarResponseBuilder
        .from(givenSolarSystems)
        .build();
    int page = 1;
    for (JsonObject response : responses) {
      wireMockServer.stubGetSolarSystems(municipalityKey, response, page++);
    }
  }

  @Given("(is, )that for the postcode {int}, the following solar systems are registered in the Marktstammdatenregister")
  public void stubSolarResponse(int postcode, DataTable givenSolarSystems) {
    List<JsonObject> responses = solarResponseBuilder
        .from(givenSolarSystems)
        .build();
    int page = 1;
    for (JsonObject response : responses) {
      wireMockServer.stubGetSolarSystems(postcode, response, page++);
    }
  }

  @Given("that in {int} in {string} with Gemeindeschlüssel {string}, {int} Balkonkraftwerke \\(with 0.6 kWp)")
  public void addBalkonkraftwerke(int year, String city, String municipalityKey, int count) {
    solarResponseBuilder
        .withYear(year)
        .withCity(city)
        .addBalkonkraftwerksWith06kWp(count);
    this.municipalityKey = municipalityKey;
  }

  @Given("that in {int} in {string} with Gemeindeschlüssel {string}, {int} homes \\(with 5.0 kWp)")
  public void addHomes(int year, String city, String municipalityKey, int count) {
    solarResponseBuilder
        .withYear(year)
        .withCity(city)
        .addHomesWith5kWp(count);
    this.municipalityKey = municipalityKey;
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
    List<JsonObject> responses = solarResponseBuilder
        .addSchoolsWith100kWp(count)
        .build();
    int page = 1;
    for (JsonObject response : responses) {
      wireMockServer.stubGetSolarSystems(municipalityKey, response, page++);
    }
  }
}
