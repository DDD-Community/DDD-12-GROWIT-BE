package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GoalDurationDto {
  @NotNull(message = "시작날짜는 필수입니다.")
  private LocalDate startDate;

  @NotNull(message = "종료날짜는 필수입니다.")
  private LocalDate endDate;
}
