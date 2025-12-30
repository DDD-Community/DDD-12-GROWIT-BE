package com.growit.app.advice.infrastructure.persistence.chatadvice;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity.ChatAdviceEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ChatAdviceDBMapper {

  public ChatAdviceEntity toEntity(ChatAdvice chatAdvice) {
    if (chatAdvice == null) return null;

    List<ChatAdviceEntity.ConversationData> conversationDataList =
        chatAdvice.getConversations() != null
            ? chatAdvice.getConversations().stream()
                .map(
                    c ->
                        new ChatAdviceEntity.ConversationData(
                            c.getWeek(),
                            c.getGoalId(),
                            c.getUserMessage(),
                            c.getGrorongResponse(),
                            c.getAdviceStyle() != null ? c.getAdviceStyle().name() : null,
                            c.getTimestamp(),
                            c.isOnboarding()))
                .collect(Collectors.toList())
            : new ArrayList<>();

    return ChatAdviceEntity.builder()
        .userId(chatAdvice.getUserId())
        .remainingCount(chatAdvice.getRemainingCount())
        .lastResetDate(chatAdvice.getLastResetDate())
        .lastConversatedAt(chatAdvice.getLastConversatedAt())
        .conversations(conversationDataList)
        .build();
  }

  public ChatAdvice toDomain(ChatAdviceEntity entity) {
    if (entity == null) return null;

    List<ChatAdvice.Conversation> conversationList =
        entity.getConversations() != null
            ? entity.getConversations().stream()
                .map(this::toConversation)
                .collect(Collectors.toList())
            : new ArrayList<>();

    return ChatAdvice.of(
        entity.getId(),
        entity.getUserId(),
        entity.getRemainingCount(),
        entity.getLastResetDate(),
        conversationList,
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getLastConversatedAt());
  }

  public void updateEntity(ChatAdviceEntity entity, ChatAdvice domain) {
    List<ChatAdviceEntity.ConversationData> conversationDataList =
        domain.getConversations() != null
            ? domain.getConversations().stream()
                .map(this::toConversationData)
                .collect(Collectors.toList())
            : new ArrayList<>();

    entity.update(
        domain.getRemainingCount(),
        domain.getLastResetDate(),
        domain.getLastConversatedAt(),
        conversationDataList);
  }

  public List<ChatAdvice> toDomainList(List<ChatAdviceEntity> entities) {
    return entities.stream().map(this::toDomain).toList();
  }

  private ChatAdvice.Conversation toConversation(ChatAdviceEntity.ConversationData data) {
    AdviceStyle adviceStyle =
        data.getAdviceStyle() != null
            ? AdviceStyle.valueOf(data.getAdviceStyle())
            : AdviceStyle.BASIC;

    return new ChatAdvice.Conversation(
        data.getWeek(),
        data.getGoalId(),
        data.getUserMessage(),
        data.getGrorongResponse(),
        adviceStyle,
        data.getTimestamp(),
        data.isOnboarding());
  }

  private ChatAdviceEntity.ConversationData toConversationData(
      ChatAdvice.Conversation conversation) {
    return new ChatAdviceEntity.ConversationData(
        conversation.getWeek(),
        conversation.getGoalId(),
        conversation.getUserMessage(),
        conversation.getGrorongResponse(),
        conversation.getAdviceStyle() != null ? conversation.getAdviceStyle().name() : null,
        conversation.getTimestamp(),
        conversation.isOnboarding());
  }
}
