package de.local.energycharts.infrastructure.mastr.gateway;

import de.local.energycharts.infrastructure.mastr.model.mapper.SolarSystemMapper;
import de.local.energycharts.solarcity.port.MastrGateway;
import lombok.Data;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "mastr")
@Data
public class MastrRestApiConfiguration {

    private String url;

    @Bean
    public MastrGateway mastrClientGateway() {
        return new MastrRestApiGateway(
                Mappers.getMapper(SolarSystemMapper.class),
                WebClient.create(url)
        );
    }
}
