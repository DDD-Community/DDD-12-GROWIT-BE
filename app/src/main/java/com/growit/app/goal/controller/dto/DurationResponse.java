package com.growit.app.goal.controller.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DurationResponse {
  private LocalDate startDate;
  private LocalDate endDate;
}
