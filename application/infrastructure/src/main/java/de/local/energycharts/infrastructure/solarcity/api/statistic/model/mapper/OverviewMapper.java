package de.local.energycharts.infrastructure.solarcity.api.statistic.model.mapper;

import de.local.energycharts.infrastructure.solarcity.api.statistic.model.OverviewResponse;
import de.local.energycharts.core.solarcity.model.Overview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OverviewMapper {

  OverviewResponse mapToResponse(Overview overview);
}
