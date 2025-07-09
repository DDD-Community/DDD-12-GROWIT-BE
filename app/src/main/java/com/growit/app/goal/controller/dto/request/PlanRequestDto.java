package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlanRequestDto {
  @NotNull(message = "{validation.goal.plan.week.required}")
  @Min(1)
  private int weekOfMonth;

  @NotBlank(message = "{validation.goal.plan.content.required}")
  @Size(min = 1, max = 20, message = "{validation.goal.plan.content.size}")
  private String content;
}
