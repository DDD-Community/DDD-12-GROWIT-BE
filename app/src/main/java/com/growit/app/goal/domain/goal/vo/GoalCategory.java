package com.growit.app.goal.domain.goal.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalCategory {
  UNCATEGORIZED("정의되지 않은 카테고리"),
  STUDY("스터디"),
  FINANCE("제테크"),
  IT_PROJECT("IT 프로젝트");

  private final String label;
}
