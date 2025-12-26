package com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "chat_advice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ChatAdviceEntity extends BaseEntity {

  @Column(name = "user_id", unique = true, nullable = false)
  private String userId;

  @Column(name = "remaining_count", nullable = false)
  private int remainingCount;

  @Column(name = "last_reset_date", nullable = false)
  private LocalDate lastResetDate;

  @Column(name = "last_conversated_at")
  private LocalDateTime lastConversatedAt;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "conversations", columnDefinition = "jsonb")
  private List<ConversationData> conversations;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public void update(
      int remainingCount,
      LocalDate lastResetDate,
      LocalDateTime lastConversatedAt,
      List<ConversationData> conversations,
      LocalDateTime updatedAt) {
    this.remainingCount = remainingCount;
    this.lastResetDate = lastResetDate;
    this.lastConversatedAt = lastConversatedAt;
    this.conversations = conversations;
    this.updatedAt = updatedAt;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConversationData {
    private Integer week;
    private String userMessage;
    private String grorongResponse;
    private String adviceStyle;
    private LocalDateTime timestamp;

    @com.fasterxml.jackson.annotation.JsonInclude(
        com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private Boolean isOnboarding = false;

    public ConversationData(
        Integer week,
        String userMessage,
        String grorongResponse,
        String adviceStyle,
        LocalDateTime timestamp,
        boolean isOnboarding) {
      this.week = week;
      this.userMessage = userMessage;
      this.grorongResponse = grorongResponse;
      this.adviceStyle = adviceStyle;
      this.timestamp = timestamp;
      this.isOnboarding = isOnboarding;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isOnboarding() {
      return isOnboarding != null && isOnboarding;
    }
  }
}
