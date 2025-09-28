package com.growit.app.retrospect.infrastructure.persistence.goalretrospect;

import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.GoalRetrospectEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GoalRetrospectDBMapper {

  public GoalRetrospectEntity toEntity(GoalRetrospect goalRetrospect) {
    if (goalRetrospect == null) return null;

    return GoalRetrospectEntity.builder()
        .uid(goalRetrospect.getId())
        .goalId(goalRetrospect.getGoalId())
        .todoCompletedRate(goalRetrospect.getTodoCompletedRate())
        .analysisSummary(goalRetrospect.getAnalysis().summary())
        .analysisAdvice(goalRetrospect.getAnalysis().advice())
        .content(goalRetrospect.getContent())
        .build();
  }

  public GoalRetrospect toDomain(GoalRetrospectEntity entity) {
    if (entity == null) return null;

    Analysis analysis = new Analysis(entity.getAnalysisSummary(), entity.getAnalysisAdvice());

    return GoalRetrospect.builder()
        .id(entity.getUid())
        .goalId(entity.getGoalId())
        .todoCompletedRate(entity.getTodoCompletedRate())
        .analysis(analysis)
        .content(entity.getContent())
        .build();
  }

  public List<GoalRetrospect> toDomainList(List<GoalRetrospectEntity> entities) {
    if (entities == null) return List.of();
    return entities.stream().map(this::toDomain).collect(Collectors.toList());
  }
}
