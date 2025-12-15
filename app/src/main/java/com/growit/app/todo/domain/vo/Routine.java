package com.growit.app.todo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Routine {
  private final RoutineDuration duration;
  private final RepeatType repeatType;

  public static Routine of(RoutineDuration duration, RepeatType repeatType) {
    return new Routine(duration, repeatType);
  }

  public boolean isValid() {
    return duration != null && duration.isValid() && repeatType != null;
  }
}