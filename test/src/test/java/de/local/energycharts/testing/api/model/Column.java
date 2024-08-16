package de.local.energycharts.testing.api.model;

import lombok.Data;

@Data
public class Column {

    private String id;
    private String name;
    private Integer year;
    private Double y; // totalInstalledMWp
    private Integer numberOfSolarSystems;
    private Long numberOfSolarSystemsUpTo1kWp;
    private Long numberOfSolarSystems1To10kWp;
    private Long numberOfSolarSystems10To40kWp;
    private Long numberOfSolarSystemsFrom40kWp;
}
