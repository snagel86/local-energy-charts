package de.local.energycharts.infrastructure.opendatasoft.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Record {

    @JsonProperty("fields")
    private Fields fields;
}
