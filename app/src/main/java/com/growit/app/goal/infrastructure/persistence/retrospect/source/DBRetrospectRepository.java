package com.growit.app.goal.infrastructure.persistence.retrospect.source;

import com.growit.app.goal.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DBRetrospectRepository extends JpaRepository<RetrospectEntity, Long> {
  Optional<RetrospectEntity> findByUid(String uid);
  
  Optional<RetrospectEntity> findByGoalIdAndPlanId(String goalId, String planId);
}