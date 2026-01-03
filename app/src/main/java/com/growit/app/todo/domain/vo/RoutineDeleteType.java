package com.growit.app.todo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoutineDeleteType {
  SINGLE("해당 투두만 삭제"),
  FROM_DATE("해당 날짜 이후 모든 투두 삭제"),
  ALL("전체 반복 투두 삭제");

  private final String description;
}
