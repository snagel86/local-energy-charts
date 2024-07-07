package de.local.energycharts.testing.service;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.response.ValidatableResponse;
import java.time.Instant;
import org.json.JSONObject;

/**
 * To test the api of this application, <a href="https://rest-assured.io/">https://rest-assured.io/</a> is used.
 * This makes it simple to create, execute and test RESTful calls.
 */
public class LocalEnergyChartsService {

  public void createSolarCity(
      String cityName,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  ) {
    JSONObject requestJson = new JSONObject();
    requestJson.put("cityName", cityName);
    if (entireSolarPotentialOnRooftopsMWp != null && targetYear != null) {
      requestJson.put("entireSolarPotentialOnRooftopsMWp", entireSolarPotentialOnRooftopsMWp);
      requestJson.put("targetYear", targetYear);
    }

    given()
        .header("Content-type", "application/json")
        .auth().basic("user", "secret")
        .body(requestJson.toString())
    .when()
        .post("/v1/solar-city/create")
    .then()
        .body(
            "cityName", is(cityName),
            "id", notNullValue()
        );
  }

  public ValidatableResponse getOverview(String cityName) {
    return when().get("/v1/solar-cities/" + cityName + "/statistics/overview").then();
  }

  public ValidatableResponse getSolarBuildingPieChart(String cityName) {
    return given()
            .get("/v1/solar-cities/" + cityName + "/statistics/building-pie-chart/highcharts")
            .then();
  }

  public ValidatableResponse getAnnualAdditionOfSolarInstallationsHighcharts(
      String cityName,
      boolean previousSolarInstallationsOnly
  ) {
    return given()
        .queryParam("previousSolarInstallationsOnly", previousSolarInstallationsOnly)
        .queryParam("years", 20)
        .get("/v1/solar-cities/" + cityName + "/statistics/annual-addition-of-solar-installations/highcharts")
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
