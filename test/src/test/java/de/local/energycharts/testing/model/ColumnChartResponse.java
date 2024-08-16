package de.local.energycharts.testing.model;

import lombok.Data;

import java.util.List;

@Data
public class ColumnChartResponse {

    private String cityName;
    private List<Column> columns;
    private Integer rooftopSolarSystemsInOperation;
    private Double installedRooftopMWpInOperation;
}
