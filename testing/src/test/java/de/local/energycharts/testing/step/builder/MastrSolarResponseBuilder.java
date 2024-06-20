package de.local.energycharts.testing.step.builder;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.MIDNIGHT;

@RequiredArgsConstructor
public class MastrSolarResponseBuilder {

  private final Integer year;
  private final Integer postcode;
  private final Map<Integer, List<JSONObject>> datas = new HashMap<>();
  private Integer lastSolarSystemId = 1;
  private Integer currentPage = 1;
  private final Integer pageSize = 1000;

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

  public void addSchoolsWith100kWp(int count) {
    for (int i = 0; i < count; ++i) {
      addSolarSystem(createSolarSystem(100.0));
    }
  }

  private void addSolarSystem(JSONObject solarSystem) {
    if (datas.get(currentPage) != null && datas.get(currentPage).size() >= pageSize) {
      currentPage++;
    }

    if (!datas.containsKey(currentPage)) {
      datas.put(currentPage, new ArrayList<>(pageSize));
    }

    datas.get(currentPage).add(solarSystem);
  }

  public List<JSONObject> build() {
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

  private JSONObject createSolarSystem(double installedNetPowerkWp) {
    JSONObject solarSystem = new JSONObject();

    solarSystem.put("Id", lastSolarSystemId++);
    solarSystem.put("Ort", "Frankfurt");
    solarSystem.put("Plz", postcode);
    solarSystem.put("EnergietraegerName", "Solare Strahlungsenergie");
    solarSystem.put("Nettonennleistung", installedNetPowerkWp);
    solarSystem.put("InbetriebnahmeDatum", getMastrDate());
    solarSystem.put("BetriebsStatusName", "In Betrieb");
    solarSystem.put("DatumLetzteAktualisierung", getMastrDate());

    return solarSystem;
  }

  private String getMastrDate() {
    return String.format(
        "/Date(%d)/",
        LocalDate.of(year, 1, 1)
            .atTime(MIDNIGHT)
            .atZone(ZoneId.of("UTC")).toInstant()
            .getEpochSecond() * 1000L
    );
  }
}
