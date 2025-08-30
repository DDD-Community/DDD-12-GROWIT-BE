package com.growit.app.mission.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMissionRequest {
  @NotNull(message = "{validation.mission.finished}")
  private Boolean finished;

  @NotBlank(message = "{validation.mission.content}")
  private String content;
}
