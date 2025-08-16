package com.growit.app.goal.domain.goal.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalStatus {
  NONE("모두 조회"),
  PROGRESS("진행중"),
  ENDED("종료");

  private final String label;
}
