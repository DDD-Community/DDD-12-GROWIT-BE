package com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "conversations", columnDefinition = "jsonb")
  private List<ConversationData> conversations;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public static ChatAdviceEntity from(ChatAdvice chatAdvice) {
    List<ConversationData> conversationDataList =
        chatAdvice.getConversations() != null
            ? chatAdvice.getConversations().stream()
                .map(
                    c ->
                        new ConversationData(
                            c.getWeek(),
                            c.getUserMessage(),
                            c.getGrorongResponse(),
                            c.getAdviceStyle() != null ? c.getAdviceStyle().name() : null,
                            c.getTimestamp()))
                .collect(Collectors.toList())
            : new ArrayList<>();

    return ChatAdviceEntity.builder()
        .userId(chatAdvice.getUserId())
        .remainingCount(chatAdvice.getRemainingCount())
        .lastResetDate(chatAdvice.getLastResetDate())
        .conversations(conversationDataList)
        .updatedAt(chatAdvice.getUpdatedAt())
        .build();
  }

  public ChatAdvice toDomain() {
    List<ChatAdvice.Conversation> conversationList =
        conversations != null
            ? conversations.stream()
                .map(
                    c -> {
                      AdviceStyle adviceStyle =
                          c.adviceStyle != null
                              ? AdviceStyle.valueOf(c.adviceStyle)
                              : AdviceStyle.BASIC;
                      return new ChatAdvice.Conversation(
                          c.week, c.userMessage, c.grorongResponse, adviceStyle, c.timestamp);
                    })
                .collect(Collectors.toList())
            : new ArrayList<>();

    return ChatAdvice.builder()
        .id(getId())
        .userId(userId)
        .remainingCount(remainingCount)
        .lastResetDate(lastResetDate)
        .conversations(conversationList)
        .createdAt(getCreatedAt())
        .updatedAt(updatedAt)
        .build();
  }

  public void updateByDomain(ChatAdvice chatAdvice) {
    this.remainingCount = chatAdvice.getRemainingCount();
    this.lastResetDate = chatAdvice.getLastResetDate();
    this.conversations =
        chatAdvice.getConversations() != null
            ? chatAdvice.getConversations().stream()
                .map(
                    c ->
                        new ConversationData(
                            c.getWeek(),
                            c.getUserMessage(),
                            c.getGrorongResponse(),
                            c.getAdviceStyle() != null ? c.getAdviceStyle().name() : null,
                            c.getTimestamp()))
                .collect(Collectors.toList())
            : new ArrayList<>();
    this.updatedAt = chatAdvice.getUpdatedAt();
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ConversationData {
    private Integer week;
    private String userMessage;
    private String grorongResponse;
    private String adviceStyle;
    private LocalDateTime timestamp;
  }
}
