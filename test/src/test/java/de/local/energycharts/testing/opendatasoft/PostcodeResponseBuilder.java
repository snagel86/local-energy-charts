package de.local.energycharts.testing.opendatasoft;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostcodeResponseBuilder {

  private final List<JSONObject> records = new ArrayList<>();
  private List<Integer> postcodes;

  public PostcodeResponseBuilder withPostcodes(List<Integer> postcodes){
    this.postcodes = postcodes;
    return this;
  }

  public JSONObject build() {
    JSONObject response = new JSONObject();

    createRecords();
    response.put("records", records);
    return response;
  }

  private void createRecords() {
    postcodes.forEach(postcode -> records.add(createRecord(postcode)));
  }

  private JSONObject createRecord(int postcode) {
    JSONObject record = new JSONObject();

    record.put("fields", createFields(postcode));
    return record;
  }

  private JSONObject createFields(int postcode) {
    JSONObject fields = new JSONObject();

    fields.put("plz_code", String.valueOf(postcode));
    return fields;
  }
}
