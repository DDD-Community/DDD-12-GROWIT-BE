package com.growit.app.retrospect.infrastructure.persistence.goalretrospect;

import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.DBGoalRetrospectRepository;
import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.GoalRetrospectEntity;
import java.time.LocalDateTime;
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
      existingEntity.updateByDomain(goalRetrospect);
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
  public List<GoalRetrospect> findAllByGoalIdAndCreatedAtBetween(String goalId, LocalDateTime start, LocalDateTime end) {
    List<GoalRetrospectEntity> entities = repository.findAllByGoalIdAndCreatedAtBetween(goalId, start, end);
    return mapper.toDomainList(entities);
  }
}
