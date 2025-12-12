package de.local.energycharts.web.api.solarcity.statistic.model.highcharts.mapper;

import de.local.energycharts.core.solarcity.model.MonthlySolarInstallations;
import de.local.energycharts.web.api.solarcity.statistic.model.highcharts.MonthlySolarInstallationsChartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ColumnMapper.class, SplineMapper.class})
public interface MonthlySolarInstallationsChartMapper {

  @Mapping(target = "columns", source = "additions")
  @Mapping(target = "splines", source = "additions")
  MonthlySolarInstallationsChartResponse mapToResponse(MonthlySolarInstallations monthlySolarInstallations);
}
