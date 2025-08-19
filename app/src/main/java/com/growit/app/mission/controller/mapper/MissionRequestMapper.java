package com.growit.app.mission.controller.mapper;

import com.growit.app.mission.controller.dto.CreateMissionRequest;
import com.growit.app.mission.controller.dto.UpdateMissionRequest;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import com.growit.app.mission.domain.dto.UpdateMissionCommand;
import org.springframework.stereotype.Component;

@Component
public class MissionRequestMapper {
  public CreateMissionCommand toCreateCommand(CreateMissionRequest request) {
    return new CreateMissionCommand(request.getFinished(), request.getContent());
  }

  public UpdateMissionCommand toUpdateCommand(
      String id, String userId, UpdateMissionRequest request) {
    return new UpdateMissionCommand(id, userId, request.getFinished());
  }
}
