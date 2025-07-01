package com.growit.retrospect.infrastructure.persistence;

import com.growit.retrospect.domain.Retrospect;
import com.growit.retrospect.infrastructure.persistence.source.entity.RetrospectEntity;
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
