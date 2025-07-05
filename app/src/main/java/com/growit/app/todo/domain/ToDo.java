package com.growit.app.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
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
  @JsonIgnore private String userId;
  private String planId;
  private String content;
  private LocalDate date;
  private boolean isCompleted;
  private boolean isDeleted;

  public static ToDo from(CreateToDoCommand command) {
    return ToDo.builder()
        .id(IDGenerator.generateId())
        .userId(command.userId())
        .goalId(command.goalId())
        .planId(command.planId())
        .content(command.content())
        .date(command.date())
        .isCompleted(false)
        .isDeleted(false)
        .build();
  }

  public void updateBy(UpdateToDoCommand command, String planId) {
    this.planId = planId;
    this.date = command.date();
    this.content = command.content();
  }

  public void updateIsCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  public void deleted() {
    this.isDeleted = true;
  }

  @JsonProperty("isCompleted")
  public boolean isCompleted() {
    return isCompleted;
  }

  @JsonIgnore
  public boolean isDeleted() {
    return isDeleted;
  }
}
