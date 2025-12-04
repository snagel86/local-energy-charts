package de.local.energycharts.infrastructure.mastr.adapter;

import de.local.energycharts.infrastructure.mastr.adapter.model.Data;
import de.local.energycharts.infrastructure.mastr.adapter.model.mapper.SolarSystemMapper;
import de.local.energycharts.core.solarcity.model.SolarSystem;
import de.local.energycharts.core.solarcity.ports.out.MastrGateway;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;

import static de.local.energycharts.infrastructure.mastr.adapter.model.Data.PAGE_SIZE;
import static java.time.format.DateTimeFormatter.ofPattern;
import static reactor.util.retry.Retry.backoff;

@SecondaryAdapter
@RequiredArgsConstructor
public class MastrRestApiGateway implements MastrGateway {

  private final SolarSystemMapper solarSystemMapper;
  private final WebClient webClient;
  private final Logger logger = LoggerFactory.getLogger(MastrRestApiGateway.class);

  public Flux<SolarSystem> getSolarSystemsByPostcode(Integer postcode) {
    var mastrFilter = "Postleitzahl~eq~'" + postcode + "'~and~Energieträger~eq~'2495'";
    return getAllSolarSystems(mastrFilter);
  }

  public Flux<SolarSystem> getSolarSystemsByMunicipalityKey(String municipalityKey, LocalDate since) {
    var mastrFilter = new StringBuilder();

    mastrFilter.append("Gemeindeschlüssel~eq~'").append(municipalityKey)
        .append("'~and~Energieträger~eq~'2495'");
    if (since != null) {
      mastrFilter.append("~and~Letzte Aktualisierung~gt~'")
          .append(since.format(ofPattern("dd.MM.yyyy"))).append("'");
    }
    return getAllSolarSystems(mastrFilter.toString());
  }

  private Flux<SolarSystem> getAllSolarSystems(String mastrFilter) {
    return getNextPage(mastrFilter, 1)
        .expand(response -> {
          if (response.getNextPage() != null) {
            return getNextPage(mastrFilter, response.getNextPage());
          }
          return Mono.empty();
        }).flatMapIterable(Data::getData)
        .map(solarSystemMapper::map);
  }

  private Mono<Data> getNextPage(String mastrFilter, Integer nextPage) {
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
        .onStatus(HttpStatusCode::isError, response -> {
          logger.error("failed to retrieved mastr data with filter {}, page={}", mastrFilter, nextPage);
          return Mono.error(new IllegalStateException(
              String.format("Failed to retrieve mastr data. filter=%s, page={%d}", mastrFilter, nextPage)
          ));
        })
        .bodyToMono(Data.class)
        .map(data -> data.setPage(nextPage))
        .timeout(Duration.ofSeconds(300))
        .retryWhen(backoff(3, Duration.ofSeconds(2)))
        .map(data -> {
          logger.info("successfully retrieved mastr data with filter {}, page={}", mastrFilter, nextPage);
          return data;
        });
  }
}
