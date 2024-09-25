package de.local.energycharts.infrastructure.opendatasoft.gateway;

import de.local.energycharts.infrastructure.opendatasoft.model.Records;
import de.local.energycharts.solarcity.port.OpendatasoftGateway;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static de.local.energycharts.infrastructure.opendatasoft.model.QueryParam.createBy;
import static de.local.energycharts.infrastructure.opendatasoft.model.Records.ROWS;
import static reactor.util.retry.Retry.backoff;

@RequiredArgsConstructor
public class OpendatasoftRestApiGateway implements OpendatasoftGateway {

  private final WebClient webClient;
  private final Logger logger = LoggerFactory.getLogger(OpendatasoftRestApiGateway.class);

  public Flux<Integer> getPostcodes(String cityName) {
    return getNextPage(cityName, 0)
        .expand(response -> {
          if (response.hasRecords()) {
            return getNextPage(cityName, response.getNextStart());
          }
          return Mono.empty();
        }).flatMapIterable(Records::getRecords)
        .map(fields -> Integer.valueOf(fields.getFields().getPlzCcode()))
        .sort();
  }

  private Mono<Records> getNextPage(String cityName, Integer start) {
    return webClient
        .mutate().codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(128 * 1024 * 1024)
        ).build()
        .get()
        .uri(builder -> builder
            .path("/api/records/1.0/search/")
            .queryParam("dataset", "georef-germany-postleitzahl")
            .queryParam("rows", ROWS)
            .queryParam("start", start)
            .queryParam(createBy(cityName).name(), cityName)
            .build()
        )
        .retrieve()
        .bodyToMono(Records.class)
        .map(records -> records.setStart(start))
        .timeout(Duration.ofSeconds(30))
        .retryWhen(backoff(3, Duration.ofSeconds(2)))
        .map(data -> {
          logger.info("successfully retrieved zip codes for city {}, start={}", cityName, start);
          return data;
        });
  }
}
