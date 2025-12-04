package de.local.energycharts.infrastructure.solarcity.api.statistic.model.highcharts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class AnnualSolarInstallationsChartResponse {

  private String cityName;
  private List<Column> columns;
  private Long rooftopSolarSystems;
  private Double installedRooftopMWp;

  @JsonIgnore
  public boolean isEmpty() {
    return columns == null || columns.isEmpty();
  }
}
