package de.local.energycharts.api.v1.solarcity.statistic.model.mapper;

import de.local.energycharts.api.v1.solarcity.statistic.model.OverviewResponse;
import de.local.energycharts.solarcity.model.Overview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OverviewMapper {

  OverviewResponse mapToResponse(Overview overview);
}
