package de.local.energycharts.infrastructure.opendatasoft.gateway;

import de.local.energycharts.solarcity.gateway.OpendatasoftGateway;
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
