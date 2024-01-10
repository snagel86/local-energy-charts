package de.local.energycharts.api.v1.solarcity.xls.service;

import de.local.energycharts.solarcity.service.SolarCityXlsService;
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
public class SolarCityXlsApiService {

    private final SolarCityXlsService solarCityXlsService;

    public Mono<ResponseEntity<?>> getSolarSystemsXls(String solarCityIdOrName) {
        return solarCityXlsService.createAllSolarSystemsXls(solarCityIdOrName)
                .map(xlsFile -> {
                    try {
                        return createOKResponseWith(xlsFile);
                    } catch (FileNotFoundException e) {
                        return ResponseEntity.internalServerError().body(e.getMessage());
                    }
                });
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
