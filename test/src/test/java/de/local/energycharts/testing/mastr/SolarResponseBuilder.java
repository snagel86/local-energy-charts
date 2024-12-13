package de.local.energycharts.testing.mastr;

import io.cucumber.datatable.DataTable;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

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

public class SolarResponseBuilder {

  private DataTable dataTable;
  private Integer year;
  private String city;
  private final Map<Integer, List<JsonObject>> datas = new HashMap<>();
  private Integer lastSolarSystemId = 1;
  private Integer currentPage = 1;
  public static final Integer PAGE_SIZE = 5000;

  public List<JsonObject> build() {
    if (dataTable != null) {
      return buildFromDataTable();
    }
    return buildFromAddedDatas();
  }

  private List<JsonObject> buildFromDataTable() {
    var jsonDataBuilder = Json.createArrayBuilder();

    dataTable.entries()
        .forEach(row -> jsonDataBuilder.add(createSolarSystem(row)));

    return singletonList(Json.createObjectBuilder()
        .add("Data", jsonDataBuilder).build()
    );
  }

  private List<JsonObject> buildFromAddedDatas() {
    List<JsonObject> responses = new ArrayList<>();
    final var total = datas.values().stream().mapToInt(List::size).sum();

    datas.forEach((page, data) -> {
      var jsonDataBuilder = Json.createArrayBuilder();
      data.forEach(jsonDataBuilder::add);

      responses.add(Json.createObjectBuilder()
          .add("Data", jsonDataBuilder.build())
          .add("Total", total)
          .build());
    });

    return responses;
  }

  public SolarResponseBuilder from(DataTable dataTable) {
    this.dataTable = dataTable;
    return this;
  }

  public SolarResponseBuilder withYear(int year) {
    this.year = year;
    return this;
  }

  public SolarResponseBuilder withCity(String city) {
    this.city = city;
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

  public SolarResponseBuilder addSchoolsWith100kWp(int count) {
    for (int i = 0; i < count; ++i) {
      addSolarSystem(createSolarSystem(100.0));
    }
    return this;
  }

  private void addSolarSystem(JsonObject solarSystem) {
    if (datas.get(currentPage) != null && datas.get(currentPage).size() >= PAGE_SIZE) {
      currentPage++;
    }

    if (!datas.containsKey(currentPage)) {
      datas.put(currentPage, new ArrayList<>(PAGE_SIZE));
    }

    datas.get(currentPage).add(solarSystem);
  }

  private JsonObject createSolarSystem(double installedNetPowerkWp) {
    return Json.createObjectBuilder()
        .add("Id", lastSolarSystemId++)
        .add("Ort", city)
        .add("EnergietraegerName", "Solare Strahlungsenergie")
        .add("Nettonennleistung", installedNetPowerkWp)
        .add("InbetriebnahmeDatum", convertToMastrDate(year))
        .add("BetriebsStatusName", "In Betrieb")
        .add("DatumLetzteAktualisierung", convertToMastrDate(year))
        .build();
  }

  private JsonObjectBuilder createSolarSystem(Map<String, String> row) {
    return Json.createObjectBuilder()
        .add("Id", row.get("Id"))
        .add("Ort", row.get("Ort"))
        .add("Plz", row.get("Plz"))
        .add("EnergietraegerName", "Solare Strahlungsenergie")
        .add("Nettonennleistung", row.get("Nettonennleistung"))
        .add("InbetriebnahmeDatum", convertToMastrDate(row.get("Inbetriebnahme")))
        .add("BetriebsStatusName", row.get("BetriebsStatus"))
        .add("DatumLetzteAktualisierung", convertToMastrDate(row.get("DatumLetzteAktualisierung")));
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
