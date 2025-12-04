package de.local.energycharts.core.solarcity.ports.in;

import de.local.energycharts.core.solarcity.model.SolarCity;
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

  /**
   * Updates all saved solar cities.
   *
   * @param full Full or partial changes since the last 3 days.
   * @return All updated solar cities.
   */
  Flux<SolarCity> updateAll(boolean full);

  void delete(String id);
}
