package com.growit.app.advice.domain.chatadvice;

import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatAdvice {
  private Long id;
  private String userId;
  private int remainingCount;
  private LocalDate lastResetDate;
  private List<Conversation> conversations;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Getter
  @AllArgsConstructor
  public static class Conversation {
    private Integer week;
    private String userMessage;
    private String grorongResponse;
    private AdviceStyle adviceStyle;
    private LocalDateTime timestamp;
  }
}
