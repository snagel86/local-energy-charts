package de.local.energycharts.testing.step;

import de.local.energycharts.testing.service.MastrRestAPIService;
import de.local.energycharts.testing.step.builder.MastrSolarResponseBuilder;
import de.local.energycharts.testing.step.converter.MastrSolarResponseConverter;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.json.JSONObject;

import java.util.List;

public class MastrStep {
  private final MastrRestAPIService mastrRestAPIService = new MastrRestAPIService();
  private MastrSolarResponseBuilder mastrSolarResponseBuilder;
  private Integer postcode;

  @Given("(is, )that for the postcode {int}, the following solar systems are registered in the Marktstammdatenregister")
  public void stubTheGivenResponse(int postcode, DataTable dataTable) {
    var response = new MastrSolarResponseConverter().convert(dataTable);
    mastrRestAPIService.stubGetSolarSystems(postcode, response.toString(), 1);
  }

  @Given("that in {int} for postcode {int}, {int} Balkonkraftwerke \\(with 0.6 kWp)")
  public void addBalkonkraftwerke(int year, int postcode, int count) {
    mastrSolarResponseBuilder = new MastrSolarResponseBuilder(year, postcode);
    mastrSolarResponseBuilder.addBalkonkraftwerksWith06kWp(count);
    this.postcode = postcode;
  }

  @Given("that in {int} for postcode {int}, {int} homes \\(with 5.0 kWp)")
  public void addHomes(int year, int postcode, int count) {
    mastrSolarResponseBuilder = new MastrSolarResponseBuilder(year, postcode);
    mastrSolarResponseBuilder.addHomesWith5kWp(count);
    this.postcode = postcode;
  }

  @And("{int} solar systems on homes \\(with 5.0 kWp)")
  public void addHomes(int count) {
    mastrSolarResponseBuilder.addHomesWith5kWp(count);
  }

  @And("{int} solar systems on apartment buildings \\(with 25.0 kWp)")
  public void addApartmentBuildings(int count) {
    mastrSolarResponseBuilder.addApartmentBuildingsWith25kWp(count);
  }

  @And("{int} solar systems on commercial buildings \\(with 100.0 kWp)")
  public void addCommercialBuildings(int count) {
    mastrSolarResponseBuilder.addCommercialBuildingsWith100kWp(count);
  }

  @And("{int} on schools \\(with 100.0 kWp) are registered in the Marktstammdatenregister")
  public void addSchoolsAndStubTheResponse(int count) {
    mastrSolarResponseBuilder.addSchoolsWith100kWp(count);
    List<JSONObject> responses = mastrSolarResponseBuilder.build();
    for (int page = 1; page < responses.size(); page++) {
      mastrRestAPIService.stubGetSolarSystems(postcode, responses.get(page).toString(), page);
    }
  }
}
