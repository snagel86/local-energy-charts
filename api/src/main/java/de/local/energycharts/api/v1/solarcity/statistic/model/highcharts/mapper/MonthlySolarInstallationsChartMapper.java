package de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.mapper;

import de.local.energycharts.api.v1.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import de.local.energycharts.solarcity.model.statistic.MonthlySolarInstallations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ColumnMapper.class, SplineMapper.class})
public interface MonthlySolarInstallationsChartMapper {

  @Mapping(target = "columns", source = "additions")
  @Mapping(target = "splines", source = "additions")
  MonthlySolarInstallationsChartResponse mapToResponse(MonthlySolarInstallations monthlySolarInstallations);
}
