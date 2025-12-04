package de.local.energycharts.infrastructure.solarcity.api.statistic.model.highcharts.mapper;

import de.local.energycharts.infrastructure.solarcity.api.statistic.model.highcharts.Spline;
import de.local.energycharts.core.solarcity.model.MonthlySolarInstallations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SplineMapper {

  @Mapping(target = "y", source = "numberOfSolarSystems")
  Spline mapToSpline(MonthlySolarInstallations.Addition addition);
}
