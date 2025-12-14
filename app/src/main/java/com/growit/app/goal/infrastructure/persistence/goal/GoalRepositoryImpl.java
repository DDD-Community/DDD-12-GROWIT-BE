package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
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
public class GoalRepositoryImpl implements GoalRepository {
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
      PlanetEntity planetEntity = findOrCreatePlanetEntity(goal.getPlanet().name());
      
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
  
  private PlanetEntity findOrCreatePlanetEntity(String planetName) {
    return planetRepository.findByName(planetName)
        .orElseGet(() -> {
          // 기본 Planet 생성
          PlanetEntity defaultPlanet = PlanetEntity.builder()
              .name(planetName)
              .imageDone("/images/" + planetName.toLowerCase() + "_done.png")
              .imageProgress("/images/" + planetName.toLowerCase() + "_progress.png")
              .description("기본 행성: " + planetName)
              .build();
          return planetRepository.save(defaultPlanet);
        });
  }
}
