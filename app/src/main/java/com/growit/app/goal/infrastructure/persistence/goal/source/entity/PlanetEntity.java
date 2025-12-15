package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.goal.planet.Planet;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "planets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanetEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @Column(nullable = false, length = 255)
  private String imageDone;

  @Column(nullable = false, length = 255)
  private String imageProgress;

  @Column(length = 500)
  private String description;

  public Planet toDomain() {
    return Planet.of(id, name, imageDone, imageProgress);
  }

  public static PlanetEntity fromDomain(Planet planet) {
    return PlanetEntity.builder()
      .id(planet.id())
        .name(planet.name())
        .imageDone(planet.imageDone())
        .imageProgress(planet.imageProgress())
        .build();
  }

  public void updateFromDomain(Planet planet) {
    this.name = planet.name();
    this.imageDone = planet.imageDone();
    this.imageProgress = planet.imageProgress();
  }
}
