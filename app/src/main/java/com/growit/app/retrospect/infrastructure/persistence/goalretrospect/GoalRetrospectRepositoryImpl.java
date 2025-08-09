package com.growit.app.retrospect.infrastructure.persistence.goalretrospect;

import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.DBGoalRetrospectRepository;
import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.GoalRetrospectEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GoalRetrospectRepositoryImpl implements GoalRetrospectRepository {
  private final GoalRetrospectDBMapper mapper;
  private final DBGoalRetrospectRepository repository;

  @Override
  public void save(GoalRetrospect goalRetrospect) {
    Optional<GoalRetrospectEntity> existing = repository.findByUid(goalRetrospect.getId());
    if (existing.isPresent()) {
      GoalRetrospectEntity existingEntity = existing.get();
      existingEntity.setGoalId(goalRetrospect.getGoalId());
      existingEntity.setTodoCompletedRate(goalRetrospect.getTodoCompletedRate());
      existingEntity.setAnalysisSummary(goalRetrospect.getAnalysis().summary());
      existingEntity.setAnalysisAdvice(goalRetrospect.getAnalysis().advice());
      existingEntity.setContent(goalRetrospect.getContent());
      repository.save(existingEntity);
    } else {
      GoalRetrospectEntity entity = mapper.toEntity(goalRetrospect);
      repository.save(entity);
    }
  }

  @Override
  public Optional<GoalRetrospect> findById(String id) {
    Optional<GoalRetrospectEntity> entity = repository.findByUid(id);
    return entity.map(mapper::toDomain);
  }

  @Override
  public Optional<GoalRetrospect> findByGoalId(String goalId) {
    Optional<GoalRetrospectEntity> entity = repository.findByGoalId(goalId);
    return entity.map(mapper::toDomain);
  }

  @Override
  public List<GoalRetrospect> findAllByGoalId(String goalId) {
    return repository.findAllByGoalId(goalId).stream().map(mapper::toDomain).toList();
  }
}
