package de.local.energycharts.infrastructure.adapter.mastr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.Accessors;

import java.util.List;

@lombok.Data
@Accessors(chain = true)
public class Data {

  public static final Integer PAGE_SIZE = 5000;

  @JsonProperty("Data")
  private List<EinheitJson> data;

  @JsonProperty("Total")
  private Integer total;

  @JsonIgnore
  private Integer page;

  public Integer getNextPage() {
    if (total == null) {
      return null;
    }
    return page * PAGE_SIZE < total ? page + 1 : null;
  }
}
