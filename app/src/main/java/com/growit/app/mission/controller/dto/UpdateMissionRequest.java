package com.growit.app.mission.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMissionRequest {
  @NotNull(message = "{validation.mission.finished}")
  private Boolean finished;
}
