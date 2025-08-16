package com.growit.app.goal.domain.goal.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalUpdateStatus {
  UPDATABLE("수정 가능"),
  PARTIALLY_UPDATABLE("수정 일부 가능"),
  ENDED("목표 종료");

  private final String label;
}
