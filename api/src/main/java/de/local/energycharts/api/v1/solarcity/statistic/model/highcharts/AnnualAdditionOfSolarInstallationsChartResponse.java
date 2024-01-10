package de.local.energycharts.api.v1.solarcity.statistic.model.highcharts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class AnnualAdditionOfSolarInstallationsChartResponse {

  private String cityName;
  private List<Column> columns;
  private Long totalSolarInstallations;
  private Double totalInstalledMWp;

  @JsonIgnore
  public boolean isEmpty() {
    return columns == null || columns.isEmpty();
  }
}
