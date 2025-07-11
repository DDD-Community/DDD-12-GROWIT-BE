package com.growit.app.todo.domain.vo;

import com.growit.app.todo.domain.ToDo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ToDoStatus {
  NONE("투두 없음"),
  NOT_STARTED("하나도 완료되지 않음"),
  IN_PROGRESS("일부 완료됨"),
  COMPLETED("모두 완료");
  private final String label;

  public static ToDoStatus getStatus(List<ToDo> todos) {
    if (todos.isEmpty()) return ToDoStatus.NONE;
    if (todos.stream().noneMatch(ToDo::isCompleted)) return ToDoStatus.NOT_STARTED;
    if (todos.stream().allMatch(ToDo::isCompleted)) return ToDoStatus.COMPLETED;
    return ToDoStatus.IN_PROGRESS;
  }
}
