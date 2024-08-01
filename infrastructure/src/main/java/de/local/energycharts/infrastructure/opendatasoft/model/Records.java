package de.local.energycharts.infrastructure.opendatasoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Data
@Accessors(chain = true)
public class Records {

  public static final Integer ROWS = 100;

  @JsonProperty("records")
  private List<Record> records;

  @JsonIgnore
  private Integer start;

  public Integer getNextStart() {
    if (start == null) {
      return 0;
    }
    return start + ROWS;
  }

  public boolean hasRecords() {
    return isNotEmpty(records);
  }
}
