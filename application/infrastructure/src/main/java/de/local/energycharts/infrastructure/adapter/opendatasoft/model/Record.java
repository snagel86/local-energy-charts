package de.local.energycharts.infrastructure.adapter.opendatasoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Record {

    @JsonProperty("fields")
    private Fields fields;
}
