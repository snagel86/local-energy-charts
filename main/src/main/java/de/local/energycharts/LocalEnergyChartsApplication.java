package de.local.energycharts;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@EnableReactiveMongoRepositories
@EnableScheduling
public class LocalEnergyChartsApplication {

  public static void main(String[] args) {
    SpringApplication.run(LocalEnergyChartsApplication.class, args);
  }
}
