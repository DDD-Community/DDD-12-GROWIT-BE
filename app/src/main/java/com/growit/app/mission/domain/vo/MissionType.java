package com.growit.app.mission.domain.vo;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MissionType {
  DAILY_TODO_WRITE("오늘의 투두 작성하기", EnumSet.allOf(DayOfWeek.class)),
  DAILY_TODO_COMPLETE("오늘의 투두 완료하기", EnumSet.allOf(DayOfWeek.class)),
  WEEKLY_GOAL_WRITE("주간 목표 작성하기", EnumSet.of(DayOfWeek.MONDAY)),
  WEEKLY_RETROSPECT_WRITE("회고 작성하기", EnumSet.of(DayOfWeek.SUNDAY));

  private final String label;
  private final Set<DayOfWeek> availableDays;
}
