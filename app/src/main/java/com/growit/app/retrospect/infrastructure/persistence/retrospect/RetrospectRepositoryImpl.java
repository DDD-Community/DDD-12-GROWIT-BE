package com.growit.app.retrospect.infrastructure.persistence.retrospect;

import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.DBRetrospectRepository;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RetrospectRepositoryImpl implements RetrospectRepository {
  private final RetrospectDBMapper mapper;
  private final DBRetrospectRepository repository;

  @Override
  public void saveRetrospect(Retrospect retrospect) {
    Optional<RetrospectEntity> existing = repository.findByUid(retrospect.getId());
    RetrospectEntity entity;
    if (existing.isPresent()) {
      entity = existing.get();
      entity.updateByDomain(retrospect);
    } else {
      entity = mapper.toEntity(retrospect);
    }
    repository.save(entity);
  }

  @Override
  public Optional<Retrospect> findById(String id) {
    Optional<RetrospectEntity> entity = repository.findByUid(id);
    return entity.map(mapper::toDomain);
  }

  @Override
  public Optional<Retrospect> findByIdAndUserId(String id, String userId) {
    Optional<RetrospectEntity> entity = repository.findByUidAndUserId(id, userId);
    return entity.map(mapper::toDomain);
  }

  @Override
  public Optional<Retrospect> findByPlanId(String planId) {
    Optional<RetrospectEntity> entity = repository.findByPlanId(planId);
    return entity.map(mapper::toDomain);
  }

  @Override
  public Optional<Retrospect> findByGoalIdAndPlanIdAndUserId(String goalId, String planId, String userId) {
    Optional<RetrospectEntity> entity = repository.findByGoalIdAndPlanIdAndUserId(goalId, planId, userId);
    return entity.map(mapper::toDomain);
  }
}
