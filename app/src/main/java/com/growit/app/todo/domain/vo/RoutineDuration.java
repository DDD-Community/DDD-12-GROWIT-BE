package com.growit.app.todo.domain.vo;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoutineDuration {
  private final LocalDate startDate;
  private final LocalDate endDate;

  public static RoutineDuration of(LocalDate startDate, LocalDate endDate) {
    return new RoutineDuration(startDate, endDate);
  }

  public boolean isValid() {
    return startDate != null && endDate != null && !endDate.isBefore(startDate);
  }
}
