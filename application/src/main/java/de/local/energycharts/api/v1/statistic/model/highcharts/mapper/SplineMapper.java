package de.local.energycharts.api.v1.statistic.model.highcharts.mapper;

import de.local.energycharts.api.v1.statistic.model.highcharts.Spline;
import de.local.energycharts.statistic.model.MonthlySolarInstallations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SplineMapper {

  @Mapping(target = "y", source = "numberOfSolarSystems")
  Spline mapToSpline(MonthlySolarInstallations.Addition addition);
}
