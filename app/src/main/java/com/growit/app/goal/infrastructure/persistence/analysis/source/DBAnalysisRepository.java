package com.growit.app.goal.infrastructure.persistence.analysis.source;

import com.growit.app.goal.infrastructure.persistence.analysis.source.entity.AnalysisEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBAnalysisRepository extends JpaRepository<AnalysisEntity, Long> {
  Optional<AnalysisEntity> findByGoalId(String goalId);

  void deleteByGoalId(String goalId);
}
