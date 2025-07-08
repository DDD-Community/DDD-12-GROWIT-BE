package com.growit.app.goal.domain.goal.vo;

import static com.growit.app.common.util.message.ErrorCode.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.exception.BadRequestException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record GoalDuration(LocalDate startDate, LocalDate endDate) {

  public GoalDuration {
    if (startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
      throw new BadRequestException(GOAL_DURATION_MONDAY.getCode());
    }
    if (endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
      throw new BadRequestException(GOAL_DURATION_SUNDAY.getCode());
    }
    if (!endDate.isAfter(startDate)) {
      throw new BadRequestException(GOAL_DURATION_START_END.getCode());
    }
  }

  @JsonIgnore
  public long getWeekCount() {
    return ChronoUnit.WEEKS.between(startDate, endDate.plusDays(1));
  }
}
