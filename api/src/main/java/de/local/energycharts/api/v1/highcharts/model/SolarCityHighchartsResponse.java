package de.local.energycharts.api.v1.highcharts.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolarCityHighchartsResponse {

  private String cityName;
  private List<AdditionOfSolarInstallationsResponse> data;
  private List<Series> drilldownData;

  private Integer totalSolarInstallations;

  public void addDrillDownSeries(Series drilldownSeries) {
    if (drilldownData == null) {
      drilldownData = new ArrayList<>();
    }
    drilldownData.add(drilldownSeries);
  }
}
