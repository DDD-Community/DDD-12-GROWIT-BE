package com.growit.app.advice.usecase.dto.ai;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiGoalRecommendationRequest {

  private String userId;
  private String promptId;
  private Input input;

  @Getter
  @Builder
  public static class Input {
    private List<String> pastTodos;
    private List<String> pastRetrospects;
    private String overallGoal;
    private List<String> completedTodos;
    private List<String> pastWeeklyGoals;
    private String remainingTime;
  }
}
