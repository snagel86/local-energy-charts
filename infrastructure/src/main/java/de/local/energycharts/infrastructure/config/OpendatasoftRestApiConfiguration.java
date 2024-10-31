package de.local.energycharts.infrastructure.config;

import de.local.energycharts.infrastructure.adapter.opendatasoft.OpendatasoftRestApiGateway;
import de.local.energycharts.solarcity.ports.out.OpendatasoftGateway;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "opendatasoft")
@Data
public class OpendatasoftRestApiConfiguration {

    private String url;

    @Bean
    public OpendatasoftGateway opendatasoftGateway() {
        return new OpendatasoftRestApiGateway(
                WebClient.create(url)
        );
    }
}
