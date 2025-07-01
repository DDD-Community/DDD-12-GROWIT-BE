package com.growit.app.goal.infrastructure.persistence.goal.retrospect;

import com.growit.app.goal.domain.goal.retrospect.Retrospect;
import com.growit.app.goal.domain.goal.retrospect.RetrospectRepository;
import com.growit.app.goal.infrastructure.persistence.goal.retrospect.source.DBRetrospectRepository;
import com.growit.app.goal.infrastructure.persistence.goal.retrospect.source.entity.RetrospectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RetrospectRepositoryImpl implements RetrospectRepository {
  
  private final RetrospectDBMapper mapper;
  private final DBRetrospectRepository repository;

  @Override
  public void saveRetrospect(Retrospect retrospect) {
    RetrospectEntity entity = mapper.toEntity(retrospect);
    repository.save(entity);
  }

  @Override
  public Optional<Retrospect> findByGoalIdAndPlanId(String goalId, String planId) {
    Optional<RetrospectEntity> entity = repository.findByGoalIdAndPlanId(goalId, planId);
    return entity.map(mapper::toDomain);
  }

  @Override
  public Optional<Retrospect> findById(String id) {
    Optional<RetrospectEntity> entity = repository.findByUid(id);
    return entity.map(mapper::toDomain);
  }
}