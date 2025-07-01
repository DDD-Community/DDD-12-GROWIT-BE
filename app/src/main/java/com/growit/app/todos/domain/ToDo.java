package com.growit.app.todos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.util.IDGenerator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ToDo {
  private String id;
  private String goalId;
  private String planId;
  private String content;
  private LocalDate date;

  @Getter(AccessLevel.NONE)
  private boolean isCompleted;

  public static ToDo from(CreateToDoCommand command) {
    return ToDo.builder()
        .id(IDGenerator.generateId())
        .goalId(command.goalId())
        .planId(command.planId())
        .content(command.content())
        .date(command.date())
        .build();
  }

  public void changed(boolean completed) {
    this.isCompleted = !completed;
  }

  @JsonIgnore
  public boolean isCompleted() {
    return isCompleted;
  }

  @JsonIgnore
  public static LocalDate[] getWeekRange(LocalDate date, int weeksAgo) {
    LocalDate baseDate = date.minusWeeks(weeksAgo);
    LocalDate weekStart = baseDate.with(DayOfWeek.MONDAY);
    LocalDate weekEnd = baseDate.with(DayOfWeek.SUNDAY);
    return new LocalDate[] {weekStart, weekEnd};
  }

  @JsonIgnore
  public static LocalDate[] getThisWeek(LocalDate date) {
    return getWeekRange(date, 0);
  }

  @JsonIgnore
  public static LocalDate[] getLastWeek(LocalDate date) {
    return getWeekRange(date, 1);
  }
}
