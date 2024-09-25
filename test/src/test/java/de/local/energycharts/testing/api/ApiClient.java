package de.local.energycharts.testing.api;

import de.local.energycharts.testing.api.model.SolarCityCreatedResponse;
import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;

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

  public void createSolarCity(JSONObject request) {
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
    JSONObject requestJson = new JSONObject();
    requestJson.put("message", message);
    requestJson.put("from", from);
    requestJson.put("to", to);

    return given()
        .header("Content-type", "application/json")
        .body(requestJson.toString())
        .when()
        .post("/v1/mail/send")
        .then();
  }

  public void freezeNowAt(Instant now) {
    JSONObject requestJson = new JSONObject();
    requestJson.put("now", now);

    given()
        .header("Content-type", "application/json")
        .body(requestJson.toString())
        .when()
        .put("/v1/test/time/freeze-now")
        .then()
        .statusCode(200);
  }
}
