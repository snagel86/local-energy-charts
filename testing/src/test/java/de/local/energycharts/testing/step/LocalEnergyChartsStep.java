package de.local.energycharts.testing.step;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import de.local.energycharts.testing.service.LocalEnergyChartsService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ValidatableResponse;
import java.util.List;

public class LocalEnergyChartsStep {

  private final LocalEnergyChartsService localEnergyChartsService = new LocalEnergyChartsService();

  private Double totalSolarPotentialMWp;
  private Integer targetYear;
  private String cityName;
  private ValidatableResponse temporaryAnnualRateOfSolarInstallations;
  private boolean writeYourLandlordReset = false;

  @And("the total solar potential of the city is {double} MWp")
  public void setTotalSolarPotential(Double totalSolarPotentialMWp) {
    this.totalSolarPotentialMWp = totalSolarPotentialMWp;
  }

  @And("the target year is {int}")
  public void setTargetYear(Integer targetYear) {
    this.targetYear = targetYear;
  }

  @When("all solar systems in {string} with the following postcodes are imported from the Marktstammdatenregister")
  public void createSolarCity(String cityName, List<Integer> postcodes) {
    this.cityName = cityName;
    localEnergyChartsService.createSolarCity(cityName, postcodes, totalSolarPotentialMWp, targetYear);
  }

  @Then("the overview has a total of {int} installed solar systems with a capacity of {float} megawatt peak")
  public void getOverview(int totalSolarInstallations, float totalInstalledMWp) {
    localEnergyChartsService.getOverview(cityName)
        .body(
            "totalSolarInstallations", is(totalSolarInstallations),
            "totalInstalledMWp", is(totalInstalledMWp)
        );
  }

  @Then("the created highcharts for the annual addition of solar installation contains the following values")
  public void getAnnualRateOfSolarInstallations(DataTable dataTable) {
    ValidatableResponse additions =
        localEnergyChartsService
            .getAnnualAdditionOfSolarInstallations(cityName, false);

    dataTable.entries().forEach(row -> {
      if (row.get("number of solar systems") != null) {
        additions.body(
            "data.name", hasItem(row.get("year")),
            "data.y", hasItem(Float.valueOf(row.get("MWp"))),
            "data.numberOfSolarSystems", hasItem(Integer.valueOf(row.get("number of solar systems")))
        );
      } else {
        additions.body(
            "data.name", hasItem(row.get("year")),
            "data.y", hasItem(Float.valueOf(row.get("MWp"))),
            "$", not(hasKey("data.numberOfSolarSystems"))
        );
      }
    });
  }

  @When("all solar systems in {string} with the following postcodes are temporary requested from the Marktstammdatenregister")
  public void createTemporaryAnnualRateOfSolarInstallations(
      String cityName,
      List<Integer> postcodes
  ) {
    this.cityName = cityName;
    temporaryAnnualRateOfSolarInstallations =
        localEnergyChartsService.createTemporaryAnnualRateOfSolarInstallations(cityName, postcodes);
  }

  @Then("the created highcharts for the temporary annual rate of solar installation contains the following values")
  public void validateTemporaryAnnualRateOfSolarInstallations(DataTable dataTable) {
    dataTable.entries().forEach(row -> temporaryAnnualRateOfSolarInstallations
        .body(
            "data.name", hasItem(row.get("year")),
            "data.y", hasItem(Float.valueOf(row.get("MWp"))),
            "data.numberOfSolarSystems", hasItem(Integer.valueOf(row.get("number of solar systems")))
        )
    );
  }

  @When("the write-your-landlord page has been (re)opened( again)")
  public void writeYourLandlordPageWasOpened() {
    if (!writeYourLandlordReset) {
      localEnergyChartsService.resetWriteYourLandlord();
      writeYourLandlordReset = true;
    }

    localEnergyChartsService.writeYourLandlordPageWasOpened();
  }

  @And("the tenant writes the following from {string} to the landlord to {string}")
  public void writeYourLandlord(String fromTenant, String toLandlord, String message) {
    if (!writeYourLandlordReset) {
      localEnergyChartsService.resetWriteYourLandlord();
      writeYourLandlordReset = true;
    }

    localEnergyChartsService.writeYourLandlord(message, fromTenant, toLandlord);
  }

  @Then("statistically the page must have been opened {int} time(s), and an email must have been sent {int} time(s)")
  public void validateWriteYourLandlordStatistic(Integer pageOpened, Integer mailSent) {
    localEnergyChartsService.getWriteYourLandlordStatistic()
        .body(
            "pageOpened", equalTo(pageOpened),
            "mailSent", equalTo(mailSent)
        );
  }
}
