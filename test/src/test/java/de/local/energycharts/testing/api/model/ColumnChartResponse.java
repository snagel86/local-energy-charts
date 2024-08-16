package de.local.energycharts.testing.api.model;

import lombok.Data;

import java.util.List;

@Data
public class ColumnChartResponse {

    private String cityName;
    private List<Column> columns;
    private Integer rooftopSolarSystemsInOperation;
    private Double installedRooftopMWpInOperation;
}
