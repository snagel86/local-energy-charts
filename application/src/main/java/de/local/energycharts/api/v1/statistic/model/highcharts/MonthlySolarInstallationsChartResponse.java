package de.local.energycharts.api.v1.statistic.model.highcharts;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class MonthlySolarInstallationsChartResponse {

  private List<Column> columns;
  private List<Spline> splines;
}
