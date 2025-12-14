package com.growit.app.goal.domain.goal.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalStatus {
  NONE("모두 조회"),
  PROGRESS("진행중"),
  IN_PROGRESS("진행중"), 
  ENDED("종료"),
  COMPLETED("완료");

  private final String label;
  
  public boolean isCompleted() {
    return this == COMPLETED || this == ENDED;
  }
  
  public boolean isInProgress() {
    return this == IN_PROGRESS || this == PROGRESS;
  }
  
  public boolean canBeUpdated() {
    return this == IN_PROGRESS || this == PROGRESS;
  }
}
