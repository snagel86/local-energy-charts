package de.local.energycharts.solarcity.ports.in;

import de.local.energycharts.solarcity.model.SolarCity;
import org.jmolecules.architecture.hexagonal.PrimaryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PrimaryPort
public interface AdministrateSolarCity {

  Mono<SolarCity> createOrUpdate(
      String name,
      String municipalityKey,
      Double entireSolarPotentialOnRooftopsMWp, Integer targetYear
  );

  Flux<Integer> getAllPostcodes(String id);

  Flux<SolarCity> getAll();

  Flux<SolarCity> updateAll();
}
