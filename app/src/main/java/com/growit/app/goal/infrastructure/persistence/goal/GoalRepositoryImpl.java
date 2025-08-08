package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.DBGoalRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepository {
  private final GoalDBMapper mapper;
  private final DBGoalRepository repository;

  @Override
  public List<Goal> findAllByUserId(String userId) {
    return repository.findByUserId(userId).stream().map(mapper::toDomain).toList();
  }

  @Override
  public void saveGoal(Goal goal) {
    Optional<GoalEntity> existing = repository.findByUid(goal.getId());
    if (existing.isPresent()) {
      GoalEntity exist = existing.get();
      exist.updateToByDomain(goal);
      repository.save(exist);
    } else {
      GoalEntity entity = mapper.toEntity(goal);
      repository.save(entity);
    }
  }

  @Override
  public Optional<Goal> findById(String uid) {
    Optional<GoalEntity> goalEntity = repository.findByUid(uid);
    return goalEntity.map(mapper::toDomain);
  }

  @Override
  public Optional<Goal> findByIdAndUserId(String id, String userId) {
    Optional<GoalEntity> goalEntity = repository.findByUidAndUserId(id, userId);
    return goalEntity.map(mapper::toDomain);
  }

  @Override
  public Optional<Goal> findByUserIdAndGoalDuration(String userId) {
    Optional<GoalEntity> goalEntity = repository.findByUserIdAndGoalDuration(userId);
    return goalEntity.map(mapper::toDomain);
  }
}
