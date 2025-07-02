package com.growit.app.todos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.util.IDGenerator;
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
  private String userId;
  private String planId;
  private String content;
  private LocalDate date;

  @Getter(AccessLevel.NONE)
  private boolean isCompleted;

  public static ToDo from(CreateToDoCommand command) {
    return ToDo.builder()
        .id(IDGenerator.generateId())
        .userId(command.userId())
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
}
