package com.growit.app.goal.infrastructure.persistence.retrospect;

import com.growit.app.goal.domain.retrospect.Retrospect;
import com.growit.app.goal.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import org.springframework.stereotype.Component;

@Component
public class RetrospectDBMapper {

  public RetrospectEntity toEntity(Retrospect retrospect) {
    return RetrospectEntity.builder()
        .uid(retrospect.getId())
        .goalId(retrospect.getGoalId())
        .planId(retrospect.getPlanId())
        .content(retrospect.getContent())
        .build();
  }

  public Retrospect toDomain(RetrospectEntity entity) {
    return Retrospect.builder()
        .id(entity.getUid())
        .goalId(entity.getGoalId())
        .planId(entity.getPlanId())
        .content(entity.getContent())
        .build();
  }
}
