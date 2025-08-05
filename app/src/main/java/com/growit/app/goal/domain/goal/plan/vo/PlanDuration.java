package com.growit.app.goal.domain.goal.plan.vo;

import java.time.LocalDate;

public record PlanDuration(LocalDate startDate, LocalDate endDate) {
  public static PlanDuration calculateDuration(int weekOfMonth, LocalDate goalStart) {
    LocalDate start = goalStart.plusWeeks(weekOfMonth - 1);
    LocalDate end = start.plusDays(6);
    return new PlanDuration(start, end);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PlanDuration that)) return false;
    return startDate.equals(that.startDate) && endDate.equals(that.endDate);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(startDate, endDate);
  }

  public boolean includes(LocalDate date) {
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }
}
