package de.local.energycharts.testing.service;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.json.JSONObject;

/**
 * To test the api of this application, https://rest-assured.io/ is used.
 * This makes it simple to create, execute and test RESTful calls.
 */
public class LocalEnergyChartsService {

  public void createSolarCity(
      String cityName, List<Integer> postcodes,
      Double totalSolarPotentialMWp, Integer totalSolarPotentialTargetYear
  ) {
    JSONObject requestJson = new JSONObject();
    requestJson.put("cityName", cityName);
    requestJson.put("postcodes", postcodes);
    if (totalSolarPotentialMWp != null && totalSolarPotentialTargetYear != null) {
      requestJson.put("totalSolarPotentialMWp", totalSolarPotentialMWp);
      requestJson.put("totalSolarPotentialTargetYear", totalSolarPotentialTargetYear);
    }

    given()
        .header("Content-type", "application/json")
        .auth().basic("user", "secret")
        .body(requestJson.toString())
    .when()
        .post("/v1/create/solar-city")
    .then()
        .body(
            "cityName", is(cityName),
            "id", notNullValue()
        );
  }

  public ValidatableResponse getOverview(String cityName) {
    return when().get("/v1/city/" + cityName + "/solar-overview").then();
  }

  public ValidatableResponse getAnnualAdditionOfSolarInstallations(
      String cityName,
      boolean previousSolarInstallationsOnly
  ) {
    return given().queryParam("previousSolarInstallationsOnly", previousSolarInstallationsOnly)
        .get("/v1/highcharts/city/" + cityName + "/annual-addition-of-solar-installations")
        .then();
  }

  public ValidatableResponse createTemporaryAnnualRateOfSolarInstallations(
      String cityName,
      List<Integer> postcodes
  ) {
    JSONObject requestJson = new JSONObject();
    requestJson.put("cityName", cityName);
    requestJson.put("postcodes", postcodes);

    return given()
        .header("Content-type", "application/json")
        .body(requestJson.toString())
    .when()
        .post("/v1/highcharts/create/temporary/annual-addition-of-solar-installations")
    .then();
  }

  public void writeYourLandlordPageWasOpened() {
    given()
        .header("Content-type", "application/json")
    .when()
        .post("/v1/write-your-landlord/opened")
    .then()
        .statusCode(200);
  }

  public void writeYourLandlord(
      String message,
      String fromTenant,
      String toLandlord
  ) {
    JSONObject requestJson = new JSONObject();
    requestJson.put("message", message);
    requestJson.put("fromTenant", fromTenant);
    requestJson.put("toLandlord", toLandlord);

    given()
        .header("Content-type", "application/json")
        .body(requestJson.toString())
    .when()
        .post("/v1/write-your-landlord/send")
    .then()
        .statusCode(200);
  }

  public ValidatableResponse getWriteYourLandlordStatistic() {
    var response = given()
        .auth().basic("user", "secret")
    .when()
        .get("/v1/write-your-landlord/statistic").then();

    given()
        .auth().basic("user", "secret")
    .when()
        .delete("/v1/write-your-landlord/reset")
    .then()
        .statusCode(200);

    return response;
  }
}
