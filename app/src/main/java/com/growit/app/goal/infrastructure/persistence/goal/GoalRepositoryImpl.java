package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.DBGoalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepository {
  private final GoalDBMapper mapper;
  private final DBGoalRepository repository;

  @Override
  public Optional<Goal> findById(String id) {
    return repository.findByUid(id).map(mapper::toDomain);
  }

  @Override
  public void saveGoal(Goal goal) {
    repository.save(mapper.toEntity(goal));
  }
}
