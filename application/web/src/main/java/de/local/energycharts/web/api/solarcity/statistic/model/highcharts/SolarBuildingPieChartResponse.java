package de.local.energycharts.web.api.solarcity.statistic.model.highcharts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class SolarBuildingPieChartResponse {

  private Double totalInstalledMWp;
  private List<PieSlice> slices;

  @JsonIgnore
  public boolean isEmpty() {
    return slices == null || slices.isEmpty();
  }
}
