package com.growit.app.advice.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAdviceResponse {
  private int remainingCount;
  private boolean isGoalOnboardingCompleted;
  private List<ConversationResponse> conversations;

  @Getter
  @AllArgsConstructor
  public static class ConversationResponse {
    private String userMessage;
    private String grorongResponse;
    private LocalDateTime timestamp;
  }
}
