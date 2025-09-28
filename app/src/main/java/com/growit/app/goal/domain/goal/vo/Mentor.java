package com.growit.app.goal.domain.goal.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Mentor {
  // STUDY (스터디)
  TIM_COOK("팀 쿡", GoalCategory.STUDY, "teamcook"),

  // FINANCE (제테크)
  WARREN_BUFFETT("워렌 버핏", GoalCategory.FINANCE, "warren-buffett"),

  // IT_PROJECT (IT 프로젝트)
  CONFUCIUS("공자", GoalCategory.IT_PROJECT, "confucius");

  private final String name;
  private final GoalCategory category;
  private final String promptIdPrefix;

  public static Mentor getMentorByCategory(GoalCategory category) {
    return java.util.Arrays.stream(values())
        .filter(mentor -> mentor.category == category)
        .findFirst()
        .orElse(TIM_COOK);
  }

  public String getAdvicePromptId() {
    return this.promptIdPrefix + "-advice-001";
  }
}
