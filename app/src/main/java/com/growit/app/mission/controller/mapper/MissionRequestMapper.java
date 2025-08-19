package com.growit.app.mission.controller.mapper;

import com.growit.app.mission.controller.dto.CreateMissionRequest;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import org.springframework.stereotype.Component;

@Component
public class MissionRequestMapper {
  public CreateMissionCommand toCommand(CreateMissionRequest request) {
    return new CreateMissionCommand(request.getFinished(), request.getContent());
  }
}
