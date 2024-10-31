package de.local.energycharts.api.v1.statistic;

import de.local.energycharts.api.v1.statistic.model.common.SolarCityOverviewResponse;
import de.local.energycharts.api.v1.statistic.model.common.mapper.SolarCityOverviewMapper;
import de.local.energycharts.statistic.ports.in.CalculateStatistic;
import de.local.energycharts.statistic.ports.in.CreateSolarXls;
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
public class SolarCityStatisticApiService {

  private final CalculateStatistic calculateStatistic;
  private final CreateSolarXls createSolarXls;
  private final SolarCityOverviewMapper solarOverviewMapper;

  public Mono<SolarCityOverviewResponse> createOverview(String id) {
    return calculateStatistic.solarCityOverview(id)
        .map(solarOverviewMapper::mapToResponse);
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
