package de.local.energycharts.infrastructure.mastr.gateway;

import de.local.energycharts.infrastructure.mastr.model.Data;
import de.local.energycharts.infrastructure.mastr.model.mapper.SolarSystemMapper;
import de.local.energycharts.solarcity.gateway.MastrGateway;
import de.local.energycharts.solarcity.model.SolarSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static de.local.energycharts.infrastructure.mastr.model.Data.PAGE_SIZE;
import static reactor.util.retry.Retry.backoff;

@RequiredArgsConstructor
public class MastrRestApiGateway implements MastrGateway {

  private final SolarSystemMapper solarSystemMapper;
  private final WebClient webClient;

  public Flux<SolarSystem> getSolarSystemsByPostcode(Integer postcode) {
    var mastrFilter = "Postleitzahl~eq~'" + postcode + "'~and~Energieträger~eq~'2495'";

    return getSolarSystems(mastrFilter, 1)
        .expand(response -> {
          if (response.getNextPage() != null) {
            return getSolarSystems(mastrFilter, response.getNextPage());
          }
          return Mono.empty();
        }).flatMapIterable(Data::getData)
        .map(solarSystemMapper::map);
  }

  public Flux<SolarSystem> getSolarSystemsByMunicipalityKey(String municipalityKey) {
    var mastrFilter = "Gemeindeschlüssel~eq~'" + municipalityKey + "'~and~Energieträger~eq~'2495'";

    return getSolarSystems(mastrFilter, 1)
        .expand(response -> {
          if (response.getNextPage() != null) {
            return getSolarSystems(mastrFilter, response.getNextPage());
          }
          return Mono.empty();
        }).flatMapIterable(Data::getData)
        .map(solarSystemMapper::map);
  }

  private Mono<Data> getSolarSystems(String mastrFilter, Integer nextPage) {
    return webClient
        .mutate().codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(128 * 1024 * 1024)
        ).build()
        .get()
        .uri(builder -> builder
            .path("/MaStR/Einheit/EinheitJson/GetVerkleinerteOeffentlicheEinheitStromerzeugung/")
            .queryParam("page", nextPage)
            .queryParam("pageSize", PAGE_SIZE)
            .queryParam("filter", mastrFilter)
            .build()
        )
        .retrieve()
        .bodyToMono(Data.class)
        .map(data -> data.setPage(nextPage))
        .timeout(Duration.ofSeconds(120))
        .retryWhen(backoff(3, Duration.ofSeconds(2)));
  }
}
