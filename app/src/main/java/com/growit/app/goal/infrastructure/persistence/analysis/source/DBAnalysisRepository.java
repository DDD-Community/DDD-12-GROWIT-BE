package com.growit.app.goal.infrastructure.persistence.analysis.source;

import com.growit.app.goal.infrastructure.persistence.analysis.source.entity.AnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DBAnalysisRepository extends JpaRepository<AnalysisEntity, Long> {
  Optional<AnalysisEntity> findByGoalId(String goalId);
  void deleteByGoalId(String goalId);
}
