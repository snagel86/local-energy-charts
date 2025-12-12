package de.local.energycharts.web.api.solarcity.statistic;

import de.local.energycharts.core.solarcity.ports.in.CalculateStatistic;
import de.local.energycharts.core.solarcity.ports.in.CreateSolarXls;
import de.local.energycharts.web.api.solarcity.statistic.model.OverviewResponse;
import de.local.energycharts.web.api.solarcity.statistic.model.mapper.OverviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class StatisticApiService {

  private final CalculateStatistic calculateStatistic;
  private final CreateSolarXls createSolarXls;
  private final OverviewMapper overviewMapper;

  public Mono<OverviewResponse> createOverview(String id) {
    return calculateStatistic.solarCityOverview(id)
        .map(overviewMapper::mapToResponse);
  }

  public Mono<ResponseEntity<?>> getSolarSystemsXls(String id) {
    return createSolarXls.allSolarSystems(id)
        .map(xlsFile -> {
          try {
            return createOKResponseWith(xlsFile);
          } catch (FileNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
          }
        })
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  private ResponseEntity<Resource> createOKResponseWith(File xlsFile) throws FileNotFoundException {
    var resource = new InputStreamResource(new FileInputStream(xlsFile));

    return ResponseEntity
        .ok()
        .header("Content-disposition", "attachment; filename=" + xlsFile.getName())
        .contentLength(xlsFile.length())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }
}
