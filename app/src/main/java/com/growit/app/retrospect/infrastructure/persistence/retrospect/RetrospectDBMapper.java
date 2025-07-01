package com.growit.app.retrospect.infrastructure.persistence.retrospect;

import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import org.springframework.stereotype.Component;

@Component
public class RetrospectDBMapper {
  public RetrospectEntity toEntity(Retrospect retrospect) {
    if (retrospect == null) return null;
    return RetrospectEntity.builder()
        .uid(retrospect.getId())
        .goalId(retrospect.getGoalId())
        .planId(retrospect.getPlanId())
        .content(retrospect.getContent())
        .build();
  }

  public Retrospect toDomain(RetrospectEntity entity) {
    if (entity == null) return null;
    return Retrospect.builder()
        .id(entity.getUid())
        .goalId(entity.getGoalId())
        .planId(entity.getPlanId())
        .content(entity.getContent())
        .build();
  }
}