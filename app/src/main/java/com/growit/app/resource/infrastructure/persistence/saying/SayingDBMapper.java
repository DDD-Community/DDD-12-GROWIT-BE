package com.growit.app.resource.infrastructure.persistence.saying;

import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.infrastructure.persistence.saying.source.entity.SayingEntity;
import org.springframework.stereotype.Component;

@Component
public class SayingDBMapper {

  public Saying toDomain(SayingEntity sayingEntity) {
    if (sayingEntity == null) return null;
    return Saying.builder()
        .id(String.valueOf(sayingEntity.getId()))
        .message(sayingEntity.getMessage())
        .from(sayingEntity.getFrom())
        .build();
  }
}
