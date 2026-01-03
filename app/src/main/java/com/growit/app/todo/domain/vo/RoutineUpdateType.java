package com.growit.app.todo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoutineUpdateType {
  SINGLE("해당 투두만 수정"),
  FROM_DATE("해당 날짜 이후 모든 투두 수정"),
  ALL("전체 반복 투두 수정");

  private final String description;
}
