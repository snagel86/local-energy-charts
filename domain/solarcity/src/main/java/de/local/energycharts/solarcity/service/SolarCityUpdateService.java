package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.model.SolarCity;
import de.local.energycharts.solarcity.repository.SolarCityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class SolarCityUpdateService {

  private final SolarCityRepository solarCityRepository;
  private final SolarCityService solarCityService;
  private final Logger logger = LoggerFactory.getLogger(SolarCityUpdateService.class.getName());

  public Flux<SolarCity> updateAllSolarCities() {
    return solarCityRepository.findAll()
        .flatMap(solarCityService::updateSolarCity)
        .onErrorContinue((err, i) -> logger.error(err.getMessage()));
  }
}
