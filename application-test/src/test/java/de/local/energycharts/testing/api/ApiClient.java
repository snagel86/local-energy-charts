package de.local.energycharts.testing.api;

import de.local.energycharts.testing.api.model.SolarCityCreatedResponse;
import io.restassured.response.ValidatableResponse;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * To test the api of this application, <a href="https://rest-assured.io/">https://rest-assured.io/</a> is used.
 * This makes it simple to create, execute and test RESTful calls.
 */
public class ApiClient {

  private String id;

  public void createSolarCity(JsonObject request) {
    var response = given()
        .header("Content-type", "application/json")
        .auth().basic("user", "secret")
        .body(request.toString())
        .when()
        .put("/v1/solar-city/create")
        .then()
        .extract().as(SolarCityCreatedResponse.class);

    assertThat(response.getId()).isNotNull();
    id = response.getId();
  }

  public void deleteSolarCity() {
    if (id == null) {
      return;
    }

    given()
        .header("Content-type", "application/json")
        .auth().basic("user", "secret")
        .when()
        .delete("/v1/solar-cities/" + id)
        .then().statusCode(200);
  }

  public ValidatableResponse getOverview() {
    return when().get("/v1/solar-cities/" + id + "/statistics/overview").then();
  }

  public ValidatableResponse getSolarBuildingPieChart() {
    return given()
        .get("/v1/solar-cities/" + id + "/statistics/building-pie-chart/highcharts")
        .then();
  }

  public ValidatableResponse getAnnualAdditionOfSolarInstallationsHighcharts(
      boolean previousSolarInstallationsOnly
  ) {
    return given()
        .queryParam("previousSolarInstallationsOnly", previousSolarInstallationsOnly)
        .queryParam("years", 20)
        .get("/v1/solar-cities/" + id + "/statistics/annual-addition-of-solar-installations/highcharts")
        .then();
  }

  public ValidatableResponse sendMail(
      String message,
      String from,
      String to
  ) {
    var requestJson = Json.createObjectBuilder()
        .add("message", message)
        .add("from", from)
        .add("to", to).build();

    return given()
        .header("Content-type", "application/json")
        .body(requestJson.toString())
        .when()
        .post("/v1/mail/send")
        .then();
  }

  public void freezeNowAt(Instant now) {
    var requestJson = Json.createObjectBuilder()
        .add("now", now.toString()).build();

    given()
        .header("Content-type", "application/json")
        .body(requestJson.toString())
        .when()
        .put("/v1/test/time/freeze-now")
        .then()
        .statusCode(200);
  }
}
