package com.growit.app.todos.domain;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.todos.domain.dto.CreateToDoCommand;
import com.growit.app.todos.domain.dto.UpdateToDoCommand;
import java.time.LocalDate;
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

  public void updateBy(UpdateToDoCommand command, String planId) {
    this.planId = planId;
    this.date = command.date();
    this.content = command.content();
  }
}
