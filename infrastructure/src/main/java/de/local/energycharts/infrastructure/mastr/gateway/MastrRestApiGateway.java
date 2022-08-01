package de.local.energycharts.infrastructure.mastr.gateway;

import de.local.energycharts.core.model.SolarSystem;
import de.local.energycharts.infrastructure.mastr.model.mapper.SolarSystemMapper;
import de.local.energycharts.mastr.gateway.MastrGateway;
import de.local.energycharts.infrastructure.mastr.model.Data;
import de.local.energycharts.infrastructure.mastr.model.EinheitJson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class MastrRestApiGateway implements MastrGateway {

  private final SolarSystemMapper solarSystemMapper;
  private final WebClient webClient;

  public Flux<SolarSystem> getSolarSystems(Integer postcode) {
    return webClient
        .mutate().codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(10 * 1024 * 1024)
        ).build()
        .get()
        .uri(builder -> builder
            .path("/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/")
            .queryParam("page", "1")
            .queryParam("pageSize", "1000000")
            .queryParam("filter", "Postleitzahl~eq~'" + postcode + "'").build()
        )
        .retrieve()
        .bodyToMono(Data.class)
        .flatMapIterable(Data::getData)
        .filter(EinheitJson::isSolareStrahlungsenergie)
        .map(solarSystemMapper::map);
  }
}
