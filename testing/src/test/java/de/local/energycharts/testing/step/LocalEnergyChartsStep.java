package de.local.energycharts.testing.step;

import de.local.energycharts.testing.service.LocalEnergyChartsService;
import de.local.energycharts.testing.step.model.Column;
import de.local.energycharts.testing.step.model.ColumnChartResponse;
import de.local.energycharts.testing.step.model.SolarBuildingPieChartResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static java.lang.Integer.parseInt;
import static java.math.RoundingMode.HALF_UP;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class LocalEnergyChartsStep {

  private final LocalEnergyChartsService localEnergyChartsService = new LocalEnergyChartsService();

  private Double totalSolarPotentialMWp;
  private Integer targetYear;
  private String cityName;
  private Integer currentYear;
  private ValidatableResponse solarBuildingPieChart;
  private ValidatableResponse writtenMailResponse;

  @And("the total solar potential on rooftops in the city is {double} MWp")
  public void setTotalSolarPotential(Double totalSolarPotentialMWp) {
    this.totalSolarPotentialMWp = totalSolarPotentialMWp;
  }

  @And("the target year is {int}")
  public void setTargetYear(Integer targetYear) {
    this.targetYear = targetYear;
  }

  @When("all solar systems in {string} are downloaded from the Marktstammdatenregister")
  public void createSolarCity(String cityName) {
    this.cityName = cityName;
    localEnergyChartsService.createSolarCity(cityName, totalSolarPotentialMWp, targetYear);
  }

  @Then("the overview has a total of {int} installed solar systems with a capacity of {float} megawatt peak")
  public void getOverview(int totalSolarInstallations, float totalInstalledMWp) {
    localEnergyChartsService.getOverview(cityName)
        .body(
            "totalRoofSolarInstallations", is(totalSolarInstallations),
            "totalInstalledRoofMWp", is(totalInstalledMWp)
        );
  }

  @Then("the calculated highchart has a total of {double} MWp and contains the following values")
  public void getAndValidateHighchart(double expectedTotalInstalledMWp, DataTable dataTable) {
    ValidatableResponse response =
        localEnergyChartsService
            .getAnnualAdditionOfSolarInstallationsHighcharts(cityName, false);

    validateTotalMWp(expectedTotalInstalledMWp, response);
    validateContainingValues(dataTable, response);
  }

  private void validateTotalMWp(double expectedTotalInstalledMWp, ValidatableResponse response) {
    var columnChartResponse = response.extract().as(ColumnChartResponse.class);

    var totalInstalledMWp = BigDecimal.valueOf(
        columnChartResponse.getColumns().stream().mapToDouble(Column::getY).sum()
    ).setScale(2, HALF_UP).doubleValue();

    assertThat(totalInstalledMWp).isEqualTo(expectedTotalInstalledMWp);
  }

  private void validateContainingValues(DataTable dataTable, ValidatableResponse response) {
    dataTable.entries().forEach(row -> response.body(
        "columns.name", hasItem(row.get("year")),
        "columns.y", hasItem(Float.valueOf(row.get("MWp"))),
        "columns.numberOfSolarSystems", hasItem(Integer.valueOf(row.get("number of solar systems")))
    ));
  }

  @And("the future available rooftop solar potential is {double} MWp")
  public void getAndValidateAndFutureAvailableSolarPotential(double expectedFutureAvailableSolarPotentialMWp) {
    var response =
        localEnergyChartsService
            .getAnnualAdditionOfSolarInstallationsHighcharts(cityName, false)
            .extract().as(ColumnChartResponse.class);

    validateAvailableFutureSolarPotential(expectedFutureAvailableSolarPotentialMWp, response);
  }

  private void validateAvailableFutureSolarPotential(double expectedFutureAvailableSolarPotentialMWp, ColumnChartResponse response) {
    var futureAvailableSolarPotentialMWp = BigDecimal.valueOf(
        response.getColumns().stream()
            .filter(column -> parseInt(column.getName() /* name = x in chart = year */) > currentYear)
            .mapToDouble(Column::getY).sum()
    ).setScale(2, HALF_UP).doubleValue();

    assertThat(futureAvailableSolarPotentialMWp)
        .isEqualTo(expectedFutureAvailableSolarPotentialMWp);
  }

  @And("the average of all yet installed solar systems is {int} kWp")
  public void getAverageOfAllYetInstalledSolarSystems(int expectedAveragekWp) {
    var response =
        localEnergyChartsService
            .getAnnualAdditionOfSolarInstallationsHighcharts(cityName, false)
            .extract().as(ColumnChartResponse.class);

    validateAverageOfAllYetInstalledSolarSystems(expectedAveragekWp, response);
  }

  private void validateAverageOfAllYetInstalledSolarSystems(int expectedAveragekWp, ColumnChartResponse response) {
    response.getColumns().stream()
        .filter(column -> parseInt(column.getName() /* name = x in chart = year */) > currentYear)
        .forEach(column -> {
          var installedMWp = column.getY();
          var numberOfSolarSystems = column.getNumberOfSolarSystems();
          var averagekWp = (int) (installedMWp / numberOfSolarSystems * 1000.0);

          assertThat(averagekWp).isEqualTo(expectedAveragekWp);
        });
  }

  @When("the tenant writes the following from {string} to the landlady*lord to {string}")
  public void tenantWritesMail(String fromTenant, String toLandlord, String message) {
    writtenMailResponse = localEnergyChartsService.sendMail(message, fromTenant, toLandlord);
  }

  @Then("the mail was successfully sent.")
  public void validateIfMailWasSent() {
    writtenMailResponse.statusCode(200);
  }

  @Then("(the pie chart must have )a slice with {int} solar installations with {float} MWp, which is {float} %")
  public void getAndValidateSolarBuildingPieChart(Integer count, Float installedMWp, Float percentage) {
    if (solarBuildingPieChart == null) {
      solarBuildingPieChart = localEnergyChartsService.getSolarBuildingPieChart(cityName);
    }
    solarBuildingPieChart
        .body(
            "slices.count", hasItem(count),
            "slices.installedMWp", hasItem(installedMWp),
            "slices.y", hasItem(percentage)
        );
  }

  @And("now frozen at {string}")
  public void freezeNowAt(String date) {
    var today = LocalDate.parse(date);
    currentYear = today.getYear();
    localEnergyChartsService.freezeNowAt(today.atStartOfDay().toInstant(UTC));
  }
}
