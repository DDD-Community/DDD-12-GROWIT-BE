package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalDurationDto {
  @NotNull(message = "{validation.goal.start-date.required}")
  private LocalDate startDate;

  @NotNull(message = "{validation.goal.end-date.required}")
  private LocalDate endDate;
}
