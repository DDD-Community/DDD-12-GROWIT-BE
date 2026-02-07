package com.growit.app.todo.domain.vo;

import com.growit.app.common.util.IDGenerator;
import java.time.DayOfWeek;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Routine {
  private String id;
  private final RoutineDuration duration;
  private final RepeatType repeatType;
  private final List<DayOfWeek> repeatDays;

  public static Routine of(
      RoutineDuration duration, RepeatType repeatType, List<DayOfWeek> repeatDays) {
    return new Routine(IDGenerator.generateId(), duration, repeatType, repeatDays);
  }

  public boolean isValid() {
    return duration != null && duration.isValid() && repeatType != null;
  }
}
