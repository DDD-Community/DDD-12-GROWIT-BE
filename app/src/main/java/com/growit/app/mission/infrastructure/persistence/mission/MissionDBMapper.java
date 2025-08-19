package com.growit.app.mission.infrastructure.persistence.mission;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.infrastructure.persistence.mission.source.entity.MissionEntity;
import org.springframework.stereotype.Component;

@Component
public class MissionDBMapper {

  public MissionEntity toEntity(Mission mission) {
    if (mission == null) return null;
    return MissionEntity.builder()
        .uid(mission.getId())
        .userId(mission.getUserId())
        .content(mission.getContent())
        .finished(mission.isFinished())
        .build();
  }

  public Mission toDomain(MissionEntity entity) {
    if (entity == null) return null;
    return Mission.builder()
        .id(entity.getUid())
        .content(entity.getContent())
        .finished(entity.isFinished())
        .build();
  }
}
