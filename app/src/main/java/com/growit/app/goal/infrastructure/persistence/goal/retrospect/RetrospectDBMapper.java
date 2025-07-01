package com.growit.app.goal.infrastructure.persistence.goal.retrospect;

import com.growit.app.goal.domain.goal.retrospect.Retrospect;
import com.growit.app.goal.infrastructure.persistence.goal.retrospect.source.entity.RetrospectEntity;
import org.springframework.stereotype.Component;

@Component
public class RetrospectDBMapper {

  public Retrospect toDomain(RetrospectEntity entity) {
    return Retrospect.builder()
        .id(entity.getUid())
        .goalId(entity.getGoalId())
        .planId(entity.getPlanId())
        .content(entity.getContent())
        .build();
  }

  public RetrospectEntity toEntity(Retrospect retrospect) {
    return RetrospectEntity.builder()
        .uid(retrospect.getId())
        .goalId(retrospect.getGoalId())
        .planId(retrospect.getPlanId())
        .content(retrospect.getContent())
        .build();
  }
}