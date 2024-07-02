package de.local.energycharts.solarcity.service;

import de.local.energycharts.solarcity.repository.SolarCityRepository;
import de.local.energycharts.solarcity.writer.SolarSystemsXlsWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
@RequiredArgsConstructor
public class SolarCityXlsService {

    private final SolarCityRepository solarCityRepository;
    private final SolarSystemsXlsWriter solarSystemsXlsWriter;

    public Mono<File> createAllSolarSystemsXls(String city) {
        return solarCityRepository.findByName(city)
                .map(solarSystemsXlsWriter::write);
    }
}
