package de.local.energycharts.web.api.solarcity.statistic.model.highcharts.mapper;

import de.local.energycharts.core.solarcity.model.SolarBuildingPieChart;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.SolarBuildingPieChartResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PieSliceMapper.class})
public interface SolarBuildingPieChartMapper {

  SolarBuildingPieChartResponse mapToResponse(SolarBuildingPieChart solarBuildingPieChart);
}
