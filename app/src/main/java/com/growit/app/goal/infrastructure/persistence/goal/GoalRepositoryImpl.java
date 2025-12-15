package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.planet.Planet;
import com.growit.app.goal.domain.goal.planet.PlanetRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.DBGoalRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.DBPlanetRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.PlanetEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepository, PlanetRepository {
  private final GoalDBMapper mapper;
  private final DBGoalRepository goalRepository;
  private final DBPlanetRepository planetRepository;

  @Override
  public List<Goal> findAllByUserId(String userId) {
    return goalRepository.findByUserId(userId).stream().map(mapper::toDomain).toList();
  }

  @Override
  public void saveGoal(Goal goal) {
    Optional<GoalEntity> existing = goalRepository.findByUid(goal.getId());
    if (existing.isPresent()) {
      GoalEntity exist = existing.get();
      mapper.updateEntity(exist, goal);
      goalRepository.save(exist);
    } else {
      // Planet 엔티티 찾기 또는 생성
      PlanetEntity planetEntity = findOrCreatePlanetEntity(goal.getPlanet().id());
      GoalEntity entity = mapper.toEntity(goal, goal.getUserId(), planetEntity);
      goalRepository.save(entity);
    }
  }

  @Override
  public Optional<Goal> findById(String uid) {
    Optional<GoalEntity> goalEntity = goalRepository.findByUid(uid);
    return goalEntity.map(mapper::toDomain);
  }

  @Override
  public Optional<Goal> findByIdAndUserId(String id, String userId) {
    Optional<GoalEntity> goalEntity = goalRepository.findByUidAndUserId(id, userId);
    return goalEntity.map(mapper::toDomain);
  }

  @Override
  public List<Goal> findByUserIdAndGoalDuration(String userId, LocalDate today) {
    List<GoalEntity> goals = goalRepository.findByUserIdAndGoalDuration(userId, today);
    return goals.stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Goal> findLastGoal(String userId) {
    Optional<GoalEntity> lastGoalEntity = goalRepository.findLastGoalByUserId(userId);
    return lastGoalEntity.map(mapper::toDomain);
  }

  private PlanetEntity findOrCreatePlanetEntity(Long id) {
    return planetRepository.findById(id).orElseThrow();
  }

  @Override
  public List<Planet> findAll() {
    return planetRepository.findAll().stream().map(PlanetEntity::toDomain).toList();
  }
}
