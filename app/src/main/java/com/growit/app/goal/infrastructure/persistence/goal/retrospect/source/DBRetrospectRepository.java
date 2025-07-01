package com.growit.app.goal.infrastructure.persistence.goal.retrospect.source;

import com.growit.app.goal.infrastructure.persistence.goal.retrospect.source.entity.RetrospectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DBRetrospectRepository extends JpaRepository<RetrospectEntity, Long> {
  Optional<RetrospectEntity> findByUid(String uid);
  
  Optional<RetrospectEntity> findByGoalIdAndPlanId(String goalId, String planId);
}