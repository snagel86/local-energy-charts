package de.local.energycharts.infrastructure.solarcity.api.statistic.model.highcharts;

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
