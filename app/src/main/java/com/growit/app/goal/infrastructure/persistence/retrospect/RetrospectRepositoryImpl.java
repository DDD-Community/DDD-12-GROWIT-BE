package com.growit.app.goal.infrastructure.persistence.retrospect;

import com.growit.app.goal.domain.retrospect.Retrospect;
import com.growit.app.goal.domain.retrospect.RetrospectRepository;
import com.growit.app.goal.infrastructure.persistence.retrospect.source.DBRetrospectRepository;
import com.growit.app.goal.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
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
      RetrospectEntity exist = existing.get();
      exist.setGoalId(retrospect.getGoalId());
      exist.setPlanId(retrospect.getPlanId());
      exist.setContent(retrospect.getContent());
      repository.save(exist);
    } else {
      RetrospectEntity entity = mapper.toEntity(retrospect);
      repository.save(entity);
    }
  }

  @Override
  public Optional<Retrospect> findByGoalIdAndPlanId(String goalId, String planId) {
    return repository.findByGoalIdAndPlanId(goalId, planId).map(mapper::toDomain);
  }
}
