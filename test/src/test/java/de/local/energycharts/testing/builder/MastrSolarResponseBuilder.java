package de.local.energycharts.testing.builder;

import io.cucumber.datatable.DataTable;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.MIDNIGHT;
import static java.util.Collections.singletonList;

public class MastrSolarResponseBuilder {

  private DataTable dataTable;
  private Integer year;
  private Integer postcode;
  private final Map<Integer, List<JSONObject>> datas = new HashMap<>();
  private Integer lastSolarSystemId = 1;
  private Integer currentPage = 1;
  public static final Integer PAGE_SIZE = 5000;

  public MastrSolarResponseBuilder from(DataTable dataTable) {
    this.dataTable = dataTable;
    return this;
  }

  public MastrSolarResponseBuilder withYear(int year) {
    this.year = year;
    return this;
  }

  public MastrSolarResponseBuilder withPostcode(int postcode) {
    this.postcode = postcode;
    return this;
  }

  public void addBalkonkraftwerksWith06kWp(int count) {
    for (int i = 0; i < count; ++i) {
      addSolarSystem(createSolarSystem(0.6));
    }
  }

  public void addHomesWith5kWp(int count) {
    for (int i = 0; i < count; ++i) {
      addSolarSystem(createSolarSystem(5.0));
    }
  }

  public void addCommercialBuildingsWith100kWp(int count) {
    for (int i = 0; i < count; ++i) {
      addSolarSystem(createSolarSystem(100.0));
    }
  }

  public void addApartmentBuildingsWith25kWp(int count) {
    for (int i = 0; i < count; ++i) {
      addSolarSystem(createSolarSystem(25.0));
    }
  }

  public MastrSolarResponseBuilder addSchoolsWith100kWp(int count) {
    for (int i = 0; i < count; ++i) {
      addSolarSystem(createSolarSystem(100.0));
    }
    return this;
  }

  public List<JSONObject> build() {
    if (dataTable != null) {
      return buildFromDataTable();
    }
    return buildFromAddedDatas();
  }

  private List<JSONObject> buildFromDataTable() {
    JSONObject response = new JSONObject();
    response.put("Data", createData(dataTable));
    return singletonList(response);
  }

  private List<JSONObject> buildFromAddedDatas() {
    List<JSONObject> responses = new ArrayList<>();
    final var total = datas.values().stream().mapToInt(List::size).sum();

    datas.forEach((page, data) -> {
      JSONObject response = new JSONObject();
      response.put("Data", data);
      response.put("Total", total);
      responses.add(response);
    });

    return responses;
  }

  private List<JSONObject> createData(DataTable dataTable) {
    List<JSONObject> data = new ArrayList<>();

    dataTable.entries().forEach(row -> data.add(createSolarSystem(row)));
    return data;
  }

  private JSONObject createSolarSystem(Map<String, String> row) {
    JSONObject solarSystem = new JSONObject();

    solarSystem.put("Id", row.get("Id"));
    solarSystem.put("Ort", row.get("Ort"));
    solarSystem.put("Plz", row.get("Plz"));
    solarSystem.put("EnergietraegerName", row.get("EnergietraegerName"));
    solarSystem.put("Nettonennleistung", row.get("Nettonennleistung"));
    solarSystem.put("InbetriebnahmeDatum", convertToMastrDate(row.get("InbetriebnahmeDatum")));
    solarSystem.put("BetriebsStatusName", row.get("BetriebsStatusName"));
    solarSystem.put("DatumLetzteAktualisierung", convertToMastrDate(row.get("DatumLetzteAktualisierung")));

    return solarSystem;
  }

  private void addSolarSystem(JSONObject solarSystem) {
    if (datas.get(currentPage) != null && datas.get(currentPage).size() >= PAGE_SIZE) {
      currentPage++;
    }

    if (!datas.containsKey(currentPage)) {
      datas.put(currentPage, new ArrayList<>(PAGE_SIZE));
    }

    datas.get(currentPage).add(solarSystem);
  }

  private JSONObject createSolarSystem(double installedNetPowerkWp) {
    JSONObject solarSystem = new JSONObject();

    solarSystem.put("Id", lastSolarSystemId++);
    solarSystem.put("Ort", "Frankfurt");
    solarSystem.put("Plz", postcode);
    solarSystem.put("EnergietraegerName", "Solare Strahlungsenergie");
    solarSystem.put("Nettonennleistung", installedNetPowerkWp);
    solarSystem.put("InbetriebnahmeDatum", convertToMastrDate(year));
    solarSystem.put("BetriebsStatusName", "In Betrieb");
    solarSystem.put("DatumLetzteAktualisierung", convertToMastrDate(year));

    return solarSystem;
  }

  private String convertToMastrDate(int year) {
    return String.format(
        "/Date(%d)/",
        LocalDate.of(year, 1, 1)
            .atTime(MIDNIGHT)
            .atZone(ZoneId.of("UTC")).toInstant()
            .getEpochSecond() * 1000L
    );
  }

  private String convertToMastrDate(String date) {
    try {
      return String.format(
          "/Date(%d)/",
          Instant.parse(date).getEpochSecond() * 1000L
      );
    } catch (DateTimeParseException e) {
      return String.format(
          "/Date(%d)/",
          LocalDate.parse(date)
              .atTime(MIDNIGHT)
              .atZone(ZoneId.of("UTC")).toInstant()
              .getEpochSecond() * 1000L
      );
    }
  }
}
