package de.local.energycharts.testing.api;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class CreateSolarCityRequestBuilder {

  private String name;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  private String municipalityKey;

  public CreateSolarCityRequestBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public CreateSolarCityRequestBuilder withEntireSolarPotentialOnRooftopsMWp(Double entireSolarPotentialOnRooftopsMWp) {
    this.entireSolarPotentialOnRooftopsMWp = entireSolarPotentialOnRooftopsMWp;
    return this;
  }

  public CreateSolarCityRequestBuilder withTargetYear(Integer targetYear) {
    this.targetYear = targetYear;
    return this;
  }

  public CreateSolarCityRequestBuilder withMunicipalityKey(String municipalityKey) {
    this.municipalityKey = municipalityKey;
    return this;
  }

  public JsonObject build() {
    var jsonBuilder = Json.createObjectBuilder();

    jsonBuilder.add("cityName", name);
    if (municipalityKey != null) {
      jsonBuilder.add("municipalityKey", municipalityKey);
    }
    if (entireSolarPotentialOnRooftopsMWp != null && targetYear != null) {
      jsonBuilder
          .add("entireSolarPotentialOnRooftopsMWp", entireSolarPotentialOnRooftopsMWp)
          .add("targetYear", targetYear);
    }
    return jsonBuilder.build();
  }
}
