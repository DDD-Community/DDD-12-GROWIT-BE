package com.growit.app.retrospect.infrastructure.persistence.retrospect;

import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import org.springframework.stereotype.Component;

@Component
public class RetrospectDBMapper {
  public RetrospectEntity toEntity(Retrospect retrospect) {
    return RetrospectEntity.builder()
        .uid(retrospect.getId())
        .userId(retrospect.getUserId())
        .goalId(retrospect.getGoalId())
        .planId(retrospect.getPlanId())
        .content(retrospect.getContent())
        .build();
  }

  public Retrospect toDomain(RetrospectEntity entity) {
    if (entity == null) return null;
    return Retrospect.builder()
        .id(entity.getUid())
        .userId(entity.getUserId())
        .goalId(entity.getGoalId())
        .planId(entity.getPlanId())
        .content(entity.getContent())
        .build();
  }
}
