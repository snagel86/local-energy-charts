package de.local.energycharts.api.v1;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${api.user}")
  private String user;
  @Value("${api.password}")
  private String password;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("*"));
    corsConfig.addAllowedHeader("*");
    corsConfig.addAllowedMethod("*");

    httpSecurity.csrf().disable()
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/v1/create/solar-city").hasRole("ADMIN")
        .antMatchers("/v1/write-your-landlord/statistic").hasRole("ADMIN")
        .antMatchers("/v1/write-your-landlord/reset").hasRole("ADMIN")
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
        .and().cors().configurationSource(request -> corsConfig);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
    authManagerBuilder.inMemoryAuthentication()
        .withUser(user)
        .password(passwordEncoder().encode(password))
        .roles("ADMIN");
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
