package de.local.energycharts.infrastructure.opendatasoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Records {

    @JsonProperty("records")
    private List<Record> records;
}
