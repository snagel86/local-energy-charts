package de.local.energycharts.infrastructure.solarcity.api.statistic.model.highcharts.mapper;

import de.local.energycharts.infrastructure.solarcity.api.statistic.model.highcharts.SolarBuildingPieChartResponse;
import de.local.energycharts.core.solarcity.model.SolarBuildingPieChart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PieSliceMapper.class})
public interface SolarBuildingPieChartMapper {

  SolarBuildingPieChartResponse mapToResponse(SolarBuildingPieChart solarBuildingPieChart);
}
