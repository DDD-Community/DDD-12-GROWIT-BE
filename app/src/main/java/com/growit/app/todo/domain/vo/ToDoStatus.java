package com.growit.app.todo.domain.vo;

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
}
