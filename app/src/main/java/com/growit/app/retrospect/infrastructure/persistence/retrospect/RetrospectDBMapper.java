package com.growit.app.retrospect.infrastructure.persistence.retrospect;

import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.vo.KPT;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectKPTEntity;
import org.springframework.stereotype.Component;

@Component
public class RetrospectDBMapper {
  public RetrospectEntity toEntity(Retrospect retrospect) {
    RetrospectEntity entity =
        RetrospectEntity.builder()
            .uid(retrospect.getId())
            .userId(retrospect.getUserId())
            .goalId(retrospect.getGoalId())
            .planId(retrospect.getPlanId())
            .content("")
            .build();

    RetrospectKPTEntity kptEntity =
        RetrospectKPTEntity.builder()
            .retrospect(entity)
            .keep(retrospect.getKpt().keep())
            .problem(retrospect.getKpt().problem())
            .tryNext(retrospect.getKpt().tryNext())
            .build();

    entity.setKpt(kptEntity);
    return entity;
  }

  public Retrospect toDomain(RetrospectEntity entity) {
    if (entity == null) return null;
    KPT kpt =
        entity.getKpt() != null ? entity.getKpt().toDomain() : new KPT("", "", entity.getContent());
    return Retrospect.builder()
        .id(entity.getUid())
        .userId(entity.getUserId())
        .goalId(entity.getGoalId())
        .planId(entity.getPlanId())
        .kpt(kpt)
        .build();
  }
}
