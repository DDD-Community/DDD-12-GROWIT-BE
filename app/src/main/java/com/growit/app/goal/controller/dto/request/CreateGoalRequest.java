package com.growit.app.goal.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGoalRequest {
  @NotBlank(message = "목표명은 필수입니다.")
  @Size(min = 1, max = 30, message = "목표명은 30자 이하여야 합니다.")
  private String name;

  @NotNull(message = "기간은 필수 입니다.")
  @Valid
  private GoalDurationDto duration;

  @NotNull(message = "목표 설정은 필수 입니다.")
  @Valid
  private BeforeAfterDto beforeAfter;

  @NotNull(message = "계획 목록은 필수입니다.")
  @Valid
  private List<PlanRequestDto> plans;
}
