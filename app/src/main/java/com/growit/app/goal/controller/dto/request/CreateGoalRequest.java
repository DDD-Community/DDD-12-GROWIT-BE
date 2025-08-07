package com.growit.app.goal.controller.dto.request;

import com.growit.app.goal.domain.goal.vo.GoalCategory;
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

  @NotNull(message = "{validation.goal.beforeafter.required}")
  @Valid
  private BeforeAfterDto beforeAfter;

  @NotNull(message = "{validation.goal.category.required}")
  @Valid
  private GoalCategory category;

  @NotNull(message = "{validation.goal.plans.required}")
  @Valid
  private List<PlanRequestDto> plans;
}
