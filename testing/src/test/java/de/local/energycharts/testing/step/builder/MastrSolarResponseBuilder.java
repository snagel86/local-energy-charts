package de.local.energycharts.testing.step.builder;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.MIDNIGHT;

@RequiredArgsConstructor
public class MastrSolarResponseBuilder {

    private final Integer year;
    private final Integer postcode;
    private List<JSONObject> data = new ArrayList<>();
    private Integer lastSolarSystemId = 1;

    public void addBalkonkraftwerksWith06kWp(int count) {
        for (int i = 0; i < count; ++i) {
            data.add(createSolarSystem(0.6));
        }
    }

    public void addHomesWith5kWp(int count) {
        for (int i = 0; i < count; ++i) {
            data.add(createSolarSystem(5.0));
        }
    }

    public void addCommercialBuildingsWith100kWp(int count) {
        for (int i = 0; i < count; ++i) {
            data.add(createSolarSystem(100.0));
        }
    }

    public void addApartmentBuildingsWith25kWp(int count) {
        for (int i = 0; i < count; ++i) {
            data.add(createSolarSystem(25.0));
        }
    }

    public void addSchoolsWith100kWp(int count) {
        for (int i = 0; i < count; ++i) {
            data.add(createSolarSystem(100.0));
        }
    }

    public JSONObject build() {
        JSONObject response = new JSONObject();

        response.put("Data", data);
        return response;
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
