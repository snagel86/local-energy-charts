package de.local.energycharts.infrastructure.solarcity.model;

import io.github.kaiso.relmongo.annotation.JoinProperty;
import io.github.kaiso.relmongo.annotation.OneToMany;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import static io.github.kaiso.relmongo.annotation.CascadeType.ALL;
import static io.github.kaiso.relmongo.annotation.FetchType.LAZY;

@Document(collection = "solarCity")
@Data
public class MongoSolarCity implements Serializable {

  @Id
  private String id;
  @Indexed(unique = true)
  private String name;
  private String municipalityKey;
  private Instant created;
  private Instant updated;
  private Double entireSolarPotentialOnRooftopsMWp;
  private Integer targetYear;
  @OneToMany(fetch= LAZY, cascade = ALL)
  @JoinProperty(name="solarSystems")
  private List<MongoSolarSystem> solarSystems;
}

