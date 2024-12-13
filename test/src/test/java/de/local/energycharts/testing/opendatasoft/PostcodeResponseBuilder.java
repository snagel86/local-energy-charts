package de.local.energycharts.testing.opendatasoft;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.util.List;

public class PostcodeResponseBuilder {

  private List<Integer> postcodes;

  public PostcodeResponseBuilder withPostcodes(List<Integer> postcodes) {
    this.postcodes = postcodes;
    return this;
  }

  public JsonObject build() {
    var jsonPostcodeBuilder = Json.createArrayBuilder();
    postcodes.forEach(postcode -> jsonPostcodeBuilder
        .add(Json.createObjectBuilder()
            .add("fields", Json.createObjectBuilder()
                .add("plz_code", postcode))
            .build()));

    return Json.createObjectBuilder()
        .add("records", jsonPostcodeBuilder).build();
  }
}
