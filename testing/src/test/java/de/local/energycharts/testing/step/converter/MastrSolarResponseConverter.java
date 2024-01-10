package de.local.energycharts.testing.step.converter;

import io.cucumber.datatable.DataTable;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.MIDNIGHT;

public class MastrSolarResponseConverter {

    public JSONObject convert(DataTable dataTable) {
        JSONObject response = new JSONObject();

        response.put("Data", createData(dataTable));
        return response;
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
