package com.growit.app.advice.usecase.dto.ai;

import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiMentorAdviceRequest {

  private String userId;
  private String promptId;
  private Input input;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    AiMentorAdviceRequest that = (AiMentorAdviceRequest) obj;
    return Objects.equals(userId, that.userId)
        && Objects.equals(promptId, that.promptId)
        && Objects.equals(input, that.input);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, promptId, input);
  }

  @Getter
  @Builder
  public static class Input {
    private List<String> weeklyRetrospects;
    private String overallGoal;
    private List<String> completedTodos;
    private List<String> incompleteTodos;
    private List<String> pastWeeklyGoals;

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Input input = (Input) obj;
      return Objects.equals(weeklyRetrospects, input.weeklyRetrospects)
          && Objects.equals(overallGoal, input.overallGoal)
          && Objects.equals(completedTodos, input.completedTodos)
          && Objects.equals(incompleteTodos, input.incompleteTodos)
          && Objects.equals(pastWeeklyGoals, input.pastWeeklyGoals);
    }

    @Override
    public int hashCode() {
      return Objects.hash(
          weeklyRetrospects, overallGoal, completedTodos, incompleteTodos, pastWeeklyGoals);
    }
  }
}
