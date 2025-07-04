package com.growit.app.goal.domain.goal.plan.vo;

import java.time.LocalDate;

public record PlanDuration(LocalDate startDate, LocalDate endDate) {
  public static PlanDuration calculateDuration(int weekOfMonth, LocalDate goalStart) {
    LocalDate start = goalStart.plusWeeks(weekOfMonth - 1);
    LocalDate end = start.plusDays(6);
    return new PlanDuration(start, end);
  }

  public boolean includes(LocalDate date) {
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }
}
