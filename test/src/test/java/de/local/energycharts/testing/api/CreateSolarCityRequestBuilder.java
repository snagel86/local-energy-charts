package de.local.energycharts.testing.api;

import org.json.JSONObject;

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

  public JSONObject build(){
    JSONObject requestJson = new JSONObject();

    requestJson.put("cityName", name);
    if (municipalityKey != null) {
      requestJson.put("municipalityKey", municipalityKey);
    }
    if (entireSolarPotentialOnRooftopsMWp != null && targetYear != null) {
      requestJson.put("entireSolarPotentialOnRooftopsMWp", entireSolarPotentialOnRooftopsMWp);
      requestJson.put("targetYear", targetYear);
    }
    return requestJson;
  }
}
