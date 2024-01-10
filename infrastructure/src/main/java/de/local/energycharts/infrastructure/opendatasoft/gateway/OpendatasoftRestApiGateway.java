package de.local.energycharts.infrastructure.opendatasoft.gateway;

import de.local.energycharts.infrastructure.opendatasoft.model.Records;
import de.local.energycharts.solarcity.gateway.OpendatasoftGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static de.local.energycharts.infrastructure.opendatasoft.model.QueryParam.createBy;
import static reactor.util.retry.Retry.backoff;

@RequiredArgsConstructor
public class OpendatasoftRestApiGateway implements OpendatasoftGateway {

  private final WebClient webClient;

  public Flux<Integer> getPostcodes(String cityName) {
    return webClient
        .mutate().codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(128 * 1024 * 1024)
        ).build()
        .get()
        .uri(builder -> builder
            .path("/api/records/1.0/search/")
            .queryParam("dataset", "georef-germany-postleitzahl")
            .queryParam("rows", "10000")
            .queryParam(createBy(cityName).name(), cityName)
            .build()
        )
        .retrieve()
        .bodyToMono(Records.class)
        .timeout(Duration.ofSeconds(30))
        .retryWhen(backoff(3, Duration.ofSeconds(2)))
        .flatMapIterable(Records::getRecords)
        .map(fields -> Integer.valueOf(fields.getFields().getPlzCcode()))
        .sort();
  }
}
