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
  @JsonProperty("isGoalOnboardingCompleted")
  private boolean isGoalOnboardingCompleted;

  @JsonProperty("isGoalOnboardingCompleted")
  public boolean isGoalOnboardingCompleted() {
    return isGoalOnboardingCompleted;
  }
}
