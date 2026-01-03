package com.growit.app.goal.infrastructure.persistence.analysis;

import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.infrastructure.persistence.analysis.source.entity.AnalysisEntity;
import org.springframework.stereotype.Component;

@Component
public class AnalysisDBMapper {

  public AnalysisEntity toEntity(String goalId, GoalAnalysis analysis) {
    if (analysis == null) return null;

    return AnalysisEntity.fromDomain(goalId, analysis);
  }

  public GoalAnalysis toDomain(AnalysisEntity entity) {
    if (entity == null) return null;

    return entity.toDomain();
  }

  public void updateEntity(AnalysisEntity entity, GoalAnalysis analysis) {
    if (entity == null || analysis == null) return;

    entity.updateFromDomain(analysis);
  }
}
