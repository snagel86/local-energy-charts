package de.local.energycharts.api.v1.statistic.model.highcharts.mapper;

import de.local.energycharts.api.v1.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.statistic.model.SolarBuildingPieChart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PieSliceMapper.class})
public interface SolarBuildingPieChartMapper {

  SolarBuildingPieChartResponse mapToResponse(SolarBuildingPieChart solarBuildingPieChart);
}
