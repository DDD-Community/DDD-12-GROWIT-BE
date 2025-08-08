package com.growit.app.goal.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGoalRequest {
  @NotBlank(message = "{validation.goal.name.required}")
  @Size(min = 1, max = 30, message = "{validation.goal.name.size}")
  private String name;

  @NotNull(message = "{validation.goal.duration.required}")
  @Valid
  private GoalDurationDto duration;

  @NotBlank(message = "{validation.goal.tobe.required}")
  @Size(min = 1, max = 30, message = "{validation.goal.tobe.size}")
  private String toBe;

  @NotNull(message = "{validation.goal.plans.required}")
  @Valid
  private List<PlanRequestDto> plans;
}
