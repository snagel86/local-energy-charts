package de.local.energycharts.testing.step;

import de.local.energycharts.testing.service.MastrRestAPIService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class MastrStep {

  private final MastrRestAPIService mastrRestAPIService = new MastrRestAPIService();

  @Given("(is, )that the Marktstammdatenregister for the postcode {int} responds with")
  public void stubTheGivenResponse(int postcode, DataTable dataTable) {
    JSONObject givenJsonResponse = new JSONObject();

    List<JSONObject> givenSolarSystemJsons = createMastrSolarSystemsResponse(dataTable);
    givenJsonResponse.put("Data", givenSolarSystemJsons);

    mastrRestAPIService.stubGetSolarSystems(postcode, givenJsonResponse.toString());
  }

  private List<JSONObject> createMastrSolarSystemsResponse(DataTable dataTable) {
    List<JSONObject> givenSolarSystemJsons = new ArrayList<>();

    dataTable.entries().forEach(row -> {
      JSONObject json = new JSONObject();

      json.put("Id", row.get("Id"));
      json.put("Ort", row.get("Ort"));
      json.put("Plz", row.get("Plz"));
      json.put("EnergietraegerName", row.get("EnergietraegerName"));
      json.put("Bruttoleistung", row.get("Bruttoleistung"));
      json.put("Nettonennleistung", row.get("Nettonennleistung"));
      json.put("InbetriebnahmeDatum", row.get("InbetriebnahmeDatum"));
      json.put("BetriebsStatusName", row.get("BetriebsStatusName"));
      json.put("DatumLetzteAktualisierung", row.get("DatumLetzteAktualisierung"));

      givenSolarSystemJsons.add(json);
    });
    return givenSolarSystemJsons;
  }
}
