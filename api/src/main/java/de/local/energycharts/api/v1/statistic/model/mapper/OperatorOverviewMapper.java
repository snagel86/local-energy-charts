package de.local.energycharts.api.v1.statistic.model.mapper;

import de.local.energycharts.api.v1.statistic.model.OperatorOverviewResponse;
import de.local.energycharts.core.model.statistic.OperatorOverview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OperatorOverviewMapper {

  OperatorOverviewResponse mapToResponse(OperatorOverview overview);
}
