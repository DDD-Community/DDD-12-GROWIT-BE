package com.growit.app.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.Routine;
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
  private String content;
  private LocalDate date;
  private boolean isCompleted;
  private boolean isDeleted;

  @JsonProperty("isImportant")
  private boolean isImportant;

  private Routine routine;

  public static ToDo from(CreateToDoCommand command) {
    return ToDo.builder()
        .id(IDGenerator.generateId())
        .userId(command.userId())
        .goalId(command.goalId())
        .content(command.content())
        .date(command.date())
        .isCompleted(false)
        .isDeleted(false)
        .isImportant(command.isImportant())
        .routine(command.routine())
        .build();
  }

  public void updateBy(UpdateToDoCommand command) {
    this.date = command.date();
    this.goalId = command.goalId();
    this.content = command.content();
    this.isImportant = command.isImportant();
    this.routine = command.routine();
  }

  public void updateContentOnly(UpdateToDoCommand command) {
    // 루틴 정보는 유지하고 내용만 변경
    this.date = command.date();
    this.goalId = command.goalId();
    this.content = command.content();
    this.isImportant = command.isImportant();
    // routine은 변경하지 않음
  }

  public void removeRoutine() {
    this.routine = null;
  }

  public void updateIsCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  public void updateIsImportant(boolean isImportant) {
    this.isImportant = isImportant;
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

  @JsonProperty("isImportant")
  public boolean isImportant() {
    return isImportant;
  }
}
