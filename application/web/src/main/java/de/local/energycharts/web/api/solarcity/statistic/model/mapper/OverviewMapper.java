package de.local.energycharts.web.api.solarcity.statistic.model.mapper;

import de.local.energycharts.core.solarcity.model.Overview;
import de.local.energycharts.web.api.solarcity.statistic.model.OverviewResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OverviewMapper {

  OverviewResponse mapToResponse(Overview overview);
}
