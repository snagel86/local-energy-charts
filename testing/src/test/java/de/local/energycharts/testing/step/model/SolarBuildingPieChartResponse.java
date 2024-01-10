package de.local.energycharts.testing.step.model;

import lombok.Data;

import java.util.List;

@Data
public class SolarBuildingPieChartResponse {

  private Double totalInstalledMWp;
  private List<SliceResponse> slices;

  @Data
  public static class SliceResponse {
    private String id;
    private SliceTypeResponse name;
    private Double y; // percentage
    private Double installedMWp;
    private Integer count;
    private Double percentage;
  }

  public enum SliceTypeResponse {
    BALKONKRAFTWERKE, HOMES, APARTMENT_BUILDINGS_COMMERCIAL_AND_CO, SCHOOLS_INDUSTRIAL_BUILDINGS_AND_CO;
  }
}
