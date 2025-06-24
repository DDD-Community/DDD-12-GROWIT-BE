package com.growit.app.goal.domain.goal.vo;

import com.growit.app.common.exception.BadRequestException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record GoalDuration(LocalDate startDate, LocalDate endDate) {

  public GoalDuration {
    if (startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
      throw new BadRequestException("목표 시작일은 월요일 이여야 합니다.");
    }
    if (endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
      throw new BadRequestException("목표 종료일은 일요일 이여야 합니다. ");
    }
    if (!endDate.isAfter(startDate)) {
      throw new BadRequestException("목표 종료일은 시작일보다 뒤여야 합니다.");
    }
  }

  public long getWeekCount() {
    return ChronoUnit.WEEKS.between(startDate, endDate.plusDays(1));
  }
}
