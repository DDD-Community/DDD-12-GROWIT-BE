package com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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

  public void update(
      int remainingCount,
      LocalDate lastResetDate,
      LocalDateTime lastConversatedAt,
      List<ConversationData> conversations) {
    this.remainingCount = remainingCount;
    this.lastResetDate = lastResetDate;
    this.lastConversatedAt = lastConversatedAt;
    this.conversations = conversations;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConversationData {
    private Integer week;
    private String goalId;
    private String userMessage;
    private String grorongResponse;
    private String adviceStyle;
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isOnboarding = false;

    public ConversationData(
        Integer week,
        String goalId,
        String userMessage,
        String grorongResponse,
        String adviceStyle,
        LocalDateTime timestamp,
        boolean isOnboarding) {
      this.week = week;
      this.goalId = goalId;
      this.userMessage = userMessage;
      this.grorongResponse = grorongResponse;
      this.adviceStyle = adviceStyle;
      this.timestamp = timestamp;
      this.isOnboarding = isOnboarding;
    }

    @JsonIgnore
    public boolean isOnboarding() {
      return isOnboarding != null && isOnboarding;
    }
  }
}
