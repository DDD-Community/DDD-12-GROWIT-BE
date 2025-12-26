package com.growit.app.advice.domain.chatadvice;

import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatAdvice {
  private final Long id;
  private final String userId;
  private final int remainingCount;
  private final LocalDate lastResetDate;
  private final List<Conversation> conversations;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final LocalDateTime lastConversatedAt;

  private ChatAdvice(
      Long id,
      String userId,
      int remainingCount,
      LocalDate lastResetDate,
      List<Conversation> conversations,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime lastConversatedAt) {

    this.id = id;
    this.userId = Objects.requireNonNull(userId, "userId cannot be null");
    this.remainingCount = remainingCount;
    this.lastResetDate = Objects.requireNonNull(lastResetDate, "lastResetDate cannot be null");
    this.conversations =
        new ArrayList<>(Objects.requireNonNullElse(conversations, Collections.emptyList()));
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.lastConversatedAt = lastConversatedAt;
  }

  @Builder
  public static ChatAdvice of(
      Long id,
      String userId,
      int remainingCount,
      LocalDate lastResetDate,
      List<Conversation> conversations,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime lastConversatedAt) {
    return new ChatAdvice(
        id,
        userId,
        remainingCount,
        lastResetDate,
        conversations,
        createdAt,
        updatedAt,
        lastConversatedAt);
  }

  public ChatAdvice addConversation(
      Integer week,
      String userMessage,
      String grorongResponse,
      AdviceStyle adviceStyle,
      boolean isOnboarding) {

    List<Conversation> updatedConversations = new ArrayList<>(this.conversations);
    updatedConversations.add(
        new Conversation(
            week, userMessage, grorongResponse, adviceStyle, LocalDateTime.now(), isOnboarding));

    return new ChatAdvice(
        this.id,
        this.userId,
        this.remainingCount - 1,
        this.lastResetDate,
        updatedConversations,
        this.createdAt,
        LocalDateTime.now(),
        LocalDateTime.now());
  }

  public boolean canSendMessage() {
    return remainingCount > 0;
  }

  public boolean needsReset(LocalDate today) {
    return !this.lastResetDate.equals(today);
  }

  public ChatAdvice resetDaily(int dailyLimit, LocalDate today) {
    return new ChatAdvice(
        this.id,
        this.userId,
        dailyLimit,
        today,
        this.conversations,
        this.createdAt,
        LocalDateTime.now(),
        this.lastConversatedAt);
  }

  @Getter
  public static class Conversation {
    private final Integer week;
    private final String userMessage;
    private final String grorongResponse;
    private final AdviceStyle adviceStyle;
    private final LocalDateTime timestamp;
    private final boolean isOnboarding;

    public Conversation(
        Integer week,
        String userMessage,
        String grorongResponse,
        AdviceStyle adviceStyle,
        LocalDateTime timestamp,
        boolean isOnboarding) {
      this.week = week;
      this.userMessage = userMessage;
      this.grorongResponse = grorongResponse;
      this.adviceStyle = adviceStyle;
      this.timestamp = timestamp;
      this.isOnboarding = isOnboarding;
    }

    public boolean isOnboarding() {
      return isOnboarding;
    }
  }
}
