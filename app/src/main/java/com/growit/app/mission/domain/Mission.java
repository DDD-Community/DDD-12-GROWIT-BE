package com.growit.app.mission.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Mission {
  private String id;
  private boolean finished;
  private String content;
  @JsonIgnore private String userId;

  public static Mission from(CreateMissionCommand command, String userId) {
    return Mission.builder()
        .id(IDGenerator.generateId())
        .content(command.content())
        .finished(command.finished())
        .userId(userId)
        .build();
  }

  public void finished(boolean finished) {
    this.finished = finished;
  }
}
