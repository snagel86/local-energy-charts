package de.local.energycharts.web.api.solarcity.statistic.model.highcharts.mapper;

import de.local.energycharts.core.solarcity.model.SolarBuildingPieChart;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.PieSlice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PieSliceMapper {

  @Mapping(target = "id", source = "type")
  @Mapping(target = "name", source = "type")
  @Mapping(target = "y", source = "percentage")
  @Mapping(target = "installedMWp", source = "installedMWp")
  @Mapping(target = "count", source = "count")
  PieSlice mapToPieSlice(SolarBuildingPieChart.Slice slice);
}
