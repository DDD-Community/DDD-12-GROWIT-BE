package com.growit.app.goal.domain.goal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record GoalDuration(LocalDate startDate, LocalDate endDate) {
  @JsonIgnore
  public long getWeekCount() {
    LocalDate firstWeekEnd = startDate.with(DayOfWeek.SUNDAY);

    if (!endDate.isAfter(firstWeekEnd)) {
      return 1;
    }

    long remainingWeeks = ChronoUnit.WEEKS.between(firstWeekEnd.plusDays(1), endDate.plusDays(1));

    return 1 + remainingWeeks;
  }
}
