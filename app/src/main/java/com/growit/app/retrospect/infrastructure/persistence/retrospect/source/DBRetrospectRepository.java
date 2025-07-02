package com.growit.app.retrospect.infrastructure.persistence.retrospect.source;

import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBRetrospectRepository extends JpaRepository<RetrospectEntity, Long> {
  Optional<RetrospectEntity> findByUid(String uid);

  Optional<RetrospectEntity> findByGoalIdAndPlanId(String goalId, String planId);
}
