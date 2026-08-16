package com.growit.app.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.Routine;
import java.time.LocalDate;
import java.time.LocalTime;
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
  private LocalTime time;
  private boolean isCompleted;
  private boolean isDeleted;

  @Builder.Default private TodoCategory category = TodoCategory.NOW;

  private Routine routine;

  public static ToDo from(CreateToDoCommand command) {
    return ToDo.builder()
        .id(IDGenerator.generateId())
        .userId(command.userId())
        .goalId(command.goalId())
        .content(command.content())
        .date(command.date())
        .time(command.time())
        .isCompleted(false)
        .isDeleted(false)
        .category(command.category() != null ? command.category() : TodoCategory.NOW)
        .routine(command.routine())
        .build();
  }

  public void updateBy(UpdateToDoCommand command) {
    this.date = command.date();
    this.time = command.time();
    this.goalId = command.goalId();
    this.content = command.content();
    this.category = command.category() != null ? command.category() : this.category;
    this.routine = command.routine();
  }

  public void updateContentOnly(UpdateToDoCommand command) {
    // 루틴 정보는 유지하고 내용만 변경
    this.date = command.date();
    this.time = command.time();
    this.goalId = command.goalId();
    this.content = command.content();
    this.category = command.category() != null ? command.category() : this.category;
    // routine은 변경하지 않음
  }

  /**
   * 일정(날짜·반복)은 그대로 두고 내용만 바꾼다.
   *
   * <p>시리즈를 일괄 수정할 때 각 회차의 날짜를 보존해야 하므로 date 를 건드리지 않는다. updateContentOnly 는 선택한 한 건을 옮길 때 쓰는 것이라
   * date 를 덮어쓴다.
   */
  public void updateContentKeepingSchedule(UpdateToDoCommand command) {
    this.goalId = command.goalId();
    this.content = command.content();
    this.isImportant = command.isImportant();
  }

  /** 이 투두가 속한 반복을 교체한다. 시리즈를 나눌 때 앞쪽 회차를 좁힌 반복으로 다시 묶는 데 쓴다. */
  public void changeRoutine(Routine routine) {
    this.routine = routine;
  }

  public void removeRoutine() {
    this.routine = null;
  }

  public void updateIsCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  public void updateCategory(TodoCategory category) {
    this.category = category;
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

  public TodoCategory getCategory() {
    return category;
  }
}
