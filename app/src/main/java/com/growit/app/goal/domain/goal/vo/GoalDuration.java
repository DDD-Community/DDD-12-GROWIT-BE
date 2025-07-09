package com.growit.app.goal.domain.goal.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record GoalDuration(LocalDate startDate, LocalDate endDate) {
  @JsonIgnore
  public long getWeekCount() {
    return ChronoUnit.WEEKS.between(startDate, endDate.plusDays(1));
  }
}
