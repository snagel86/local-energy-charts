package de.local.energycharts.infrastructure.solarcity.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

  @Value("${api.user}")
  private String user;
  @Value("${api.password}")
  private String password;

  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    UserDetails admin = User
        .withUsername(user)
        .password(passwordEncoder().encode(password))
        .roles("ADMIN")
        .build();

    return new MapReactiveUserDetailsService(admin);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

    return http
        .headers(headers -> headers.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers(HttpMethod.PUT, "/v1/solar-city/create").hasRole("ADMIN")
            .pathMatchers(HttpMethod.DELETE, "/v1/solar-cities/{id}").hasRole("ADMIN")
            .pathMatchers("/**").permitAll()
            .anyExchange().authenticated()
        )
        .httpBasic(withDefaults())
        .build();
  }
}
