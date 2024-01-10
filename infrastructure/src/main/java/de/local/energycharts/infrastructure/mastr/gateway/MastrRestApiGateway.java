package de.local.energycharts.infrastructure.mastr.gateway;

import static reactor.util.retry.Retry.backoff;

import de.local.energycharts.solarcity.model.SolarSystem;
import de.local.energycharts.infrastructure.mastr.model.Data;
import de.local.energycharts.infrastructure.mastr.model.EinheitJson;
import de.local.energycharts.infrastructure.mastr.model.mapper.SolarSystemMapper;
import de.local.energycharts.solarcity.gateway.MastrGateway;
import java.time.Duration;
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
            .maxInMemorySize(128 * 1024 * 1024)
        ).build()
        .get()
        .uri(builder -> builder
            .path("/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/")
            .queryParam("page", "1")
            .queryParam("pageSize", "1000000")
            .queryParam("filter", "Postleitzahl~eq~'" + postcode + "'~and~Energieträger~eq~'2495'")
            .build()
        )
        .retrieve()
        .bodyToMono(Data.class)
        .timeout(Duration.ofSeconds(30))
        .retryWhen(backoff(3, Duration.ofSeconds(2)))
        .flatMapIterable(Data::getData)
        .filter(EinheitJson::isSolareStrahlungsenergie)
        .map(solarSystemMapper::map);
  }
}
