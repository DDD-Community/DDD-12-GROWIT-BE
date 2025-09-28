package com.growit.app.advice.domain.mentor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MentorType {
  TEAM_COOK("teamcook");

  private final String promptIdPrefix;

  public String getGoalPromptId() {
    return this.promptIdPrefix + "-goal-001";
  }

  public String getAdvicePromptId() {
    return this.promptIdPrefix + "-001";
  }
}
