package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.PlanetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DBPlanetRepository extends JpaRepository<PlanetEntity, Long> {
  Optional<PlanetEntity> findByName(String name);
  List<PlanetEntity> findAllByOrderById();
}