package com.growit.app.advice.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAdviceResponse {
  private int remainingCount;
  private boolean isGoalOnboardingCompleted;
  private List<ConversationResponse> conversations;

  public int getRemainingCount() {
    return remainingCount;
  }

  @JsonProperty("isGoalOnboardingCompleted")
  public boolean isGoalOnboardingCompleted() {
    return isGoalOnboardingCompleted;
  }

  public List<ConversationResponse> getConversations() {
    return conversations;
  }

  @Getter
  @AllArgsConstructor
  public static class ConversationResponse {
    private String userMessage;
    private String grorongResponse;
    private LocalDateTime timestamp;
  }
}
