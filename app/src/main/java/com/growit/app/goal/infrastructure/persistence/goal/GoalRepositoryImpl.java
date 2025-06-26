package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.DBGoalRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepository {
  private final GoalDBMapper mapper;
  private final DBGoalRepository repository;

  @Override
  public List<Goal> findAllByUserIdAndDeletedAtIsNull(String userId) {
    return repository.findWithPlansByUserIdAndDeletedAtIsNull(userId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void saveGoal(Goal goal) {
    GoalEntity entity = mapper.toEntity(goal);
    Optional<GoalEntity> existing = repository.findByUid(entity.getUid());

    if (existing.isPresent()) {
      GoalEntity exist = existing.get();
      if (goal.isDelete()) {
        exist.setDeletedAt(LocalDateTime.now());
      }
      repository.save(exist);
    } else {
      repository.save(entity);
    }
  }

  @Override
  public Optional<Goal> findByUid(String uid) {
    Optional<GoalEntity> goalEntity = repository.findByUid(uid);
    return goalEntity.map(mapper::toDomain);
  }
}
