package com.growit.app.advice.usecase.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatAdviceRequest {
  private String userId;
  private String goalId;
  private String goalTitle;
  private String concern;
  private String mode;
  private List<String> recentTodos;
  private List<String> activeGoals;
  private String yesterdayConversation;
  private Integer week;
  private String birthDate;
  private String birthTime;
  private String gender;

  @JsonProperty("isGoalOnboardingCompleted")
  private boolean isGoalOnboardingCompleted;

  @JsonProperty("isGoalOnboardingCompleted")
  public boolean isGoalOnboardingCompleted() {
    return isGoalOnboardingCompleted;
  }

  private ManseRyok manseRyok;

  @Getter
  @Builder
  public static class ManseRyok {
    private String year;
    private String month;
    private String day;
    private String hour;
  }
}
