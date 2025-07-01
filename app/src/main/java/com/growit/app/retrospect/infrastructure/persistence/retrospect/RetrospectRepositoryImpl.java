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
    if (existing.isPresent()) {
      RetrospectEntity entity = existing.get();
      entity.setGoalId(retrospect.getGoalId());
      entity.setPlanId(retrospect.getPlanId());
      entity.setContent(retrospect.getContent());
      repository.save(entity);
    } else {
      RetrospectEntity entity = mapper.toEntity(retrospect);
      repository.save(entity);
    }
  }

  @Override
  public Optional<Retrospect> findByGoalIdAndPlanId(String goalId, String planId) {
    Optional<RetrospectEntity> entity = repository.findByGoalIdAndPlanId(goalId, planId);
    return entity.map(mapper::toDomain);
  }
}