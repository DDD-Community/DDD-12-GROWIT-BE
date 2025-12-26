package com.growit.app.advice.batch.processor;

import com.growit.app.advice.domain.chatadvice.service.ChatAdviceDataCollector;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceDataCollector.MorningAdviceData;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
@RequiredArgsConstructor
public class MorningAdviceProcessor implements ItemProcessor<UserEntity, ChatAdviceRequest> {

  private final ChatAdviceDataCollector chatAdviceDataCollector;

  @Override
  public ChatAdviceRequest process(UserEntity userEntity) throws Exception {
    User user = userEntity.toDomain();
    log.debug("Processing morning advice for user: {}", user.getId());

    MorningAdviceData data = chatAdviceDataCollector.collectMorningAdviceData(user);

    return ChatAdviceRequest.builder()
        .userId(user.getId())
        .mode("MORNING")
        .activeGoals(data.getActiveGoals())
        .recentTodos(data.getRecentTodos())
        .yesterdayConversation(data.getYesterdayConversation())
        .isGoalOnboardingCompleted(true)
        .build();
  }
}
