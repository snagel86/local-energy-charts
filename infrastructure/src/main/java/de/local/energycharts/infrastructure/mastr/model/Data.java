package de.local.energycharts.infrastructure.mastr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@lombok.Data
public class Data {

  @JsonProperty("Data")
  private List<EinheitJson> data;
}
