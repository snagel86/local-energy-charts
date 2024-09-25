package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.port.SolarCityRepository;
import de.local.energycharts.solarcity.port.SolarSystemsXlsWriter;
import de.local.energycharts.solarcity.usecase.CreateAllSolarSystemsXls;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
@RequiredArgsConstructor
public class SolarCityXlsService implements CreateAllSolarSystemsXls {

  private final SolarCityRepository solarCityRepository;
  private final SolarSystemsXlsWriter solarSystemsXlsWriter;

  public Mono<File> createAllSolarSystemsXls(String id) {
    return solarCityRepository.findById(id)
        .map(solarSystemsXlsWriter::write);
  }
}
