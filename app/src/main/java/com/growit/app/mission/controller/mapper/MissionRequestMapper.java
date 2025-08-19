package com.growit.app.mission.controller.mapper;

import com.growit.app.mission.controller.dto.CreateMissionRequest;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import org.springframework.stereotype.Component;

@Component
public class MissionRequestMapper {
  public CreateMissionCommand toCommand(String userId, CreateMissionRequest request) {
    return new CreateMissionCommand(userId, request.getFinished(), request.getContent());
  }
}
