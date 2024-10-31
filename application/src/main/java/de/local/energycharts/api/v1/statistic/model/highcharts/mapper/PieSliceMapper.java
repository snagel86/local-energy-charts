package de.local.energycharts.api.v1.statistic.model.highcharts.mapper;

import de.local.energycharts.api.v1.statistic.model.highcharts.PieSlice;
import de.local.energycharts.statistic.model.SolarBuildingPieChart;
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
