package de.local.energycharts.infrastructure.opendatasoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Fields {

    @JsonProperty("plz_code")
    private String plzCcode;
}
