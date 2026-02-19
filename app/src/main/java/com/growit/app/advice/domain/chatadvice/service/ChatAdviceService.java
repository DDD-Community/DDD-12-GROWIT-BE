package com.growit.app.advice.domain.chatadvice.service;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.user.User;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatAdviceService implements ChatAdviceValidator {

  private static final int DAILY_LIMIT = 3;

  private final ChatAdviceRepository chatAdviceRepository;
  private final ChatAdviceClient chatAdviceClient;
  private final ChatAdviceDataCollector dataCollector;
  private final Clock clock;

  public ChatAdvice prepareForNewMessage(String userId) {
    ChatAdvice chatAdvice = getOrCreateChatAdvice(userId);
    chatAdvice = resetIfNeeded(chatAdvice);
    validateCanSendMessage(chatAdvice);
    return chatAdvice;
  }

  public ChatAdvice addAdviceConversation(
      ChatAdvice chatAdvice,
      User user,
      Integer week,
      String goalId,
      String userMessage,
      AdviceStyle adviceStyle,
      Boolean isOnboarding) {

    validateConversation(userMessage, week);

    ChatAdviceRequest request =
        buildAiRequest(user, week, goalId, userMessage, adviceStyle, isOnboarding);

    String aiAdvice = requestAiAdvice(request);

    ChatAdvice updated =
        chatAdvice.addConversation(
            week, goalId, userMessage, aiAdvice, adviceStyle, Boolean.TRUE.equals(isOnboarding));

    chatAdviceRepository.save(updated);
    return updated;
  }

  @Override
  public void validateCanSendMessage(ChatAdvice chatAdvice) {
    if (!chatAdvice.canSendMessage()) {
      throw new BadRequestException("일일 대화 횟수를 초과했습니다.");
    }
  }

  @Override
  public void validateConversation(String userMessage, Integer week) {
    if (userMessage == null || userMessage.isBlank()) {
      throw new BadRequestException("메시지는 비어있을 수 없습니다.");
    }
    if (week == null || week < 1) {
      throw new BadRequestException("유효하지 않은 주차입니다.");
    }
  }

  public ChatAdvice getOrCreateChatAdvice(String userId) {
    LocalDate today = LocalDate.now(clock);
    return chatAdviceRepository
        .findByUserId(userId)
        .orElseGet(() -> createNewChatAdvice(userId, today));
  }

  public ChatAdvice resetIfNeeded(ChatAdvice chatAdvice) {
    LocalDate today = LocalDate.now(clock);
    if (chatAdvice.needsReset(today)) {
      return chatAdvice.resetDaily(DAILY_LIMIT, today);
    }
    return chatAdvice;
  }

  /** 조회 시점에 날짜가 지났는지 확인하고, 지났다면 리셋 후 DB에 저장합니다. */
  public ChatAdvice checkAndResetDailyLimit(ChatAdvice chatAdvice) {
    if (chatAdvice == null) {
      return null;
    }

    LocalDate today = LocalDate.now(clock);
    if (chatAdvice.needsReset(today)) {
      ChatAdvice resetAdvice = chatAdvice.resetDaily(DAILY_LIMIT, today);
      chatAdviceRepository.save(resetAdvice);
      return resetAdvice;
    }

    return chatAdvice;
  }

  private ChatAdviceRequest buildAiRequest(
      User user,
      Integer week,
      String goalId,
      String userMessage,
      AdviceStyle adviceStyle,
      Boolean isOnboarding) {

    ChatAdviceDataCollector.RealtimeAdviceData data =
        dataCollector.collectRealtimeData(user, goalId, userMessage);

    return ChatAdviceRequest.builder()
        .userId(user.getId())
        .goalId(data.getGoalId())
        .goalTitle(data.getSelectedGoal())
        .concern(data.getUserMessage())
        .mode(adviceStyle.getLabel())
        .week(week)
        .recentTodos(data.getRecentTodos())
        .isGoalOnboardingCompleted(isOnboarding == null || !isOnboarding)
        .birthDate(
            user.getSaju() != null && user.getSaju().birth() != null
                ? user.getSaju().birth().toString()
                : null)
        .birthTime(
            user.getSaju() != null && user.getSaju().birthHour() != null
                ? user.getSaju().birthHour().getLabel()
                : null)
        .gender(
            user.getSaju() != null && user.getSaju().gender() != null
                ? user.getSaju().gender().getLabel()
                : null)
        .manseRyok(
            user.getSaju() != null
                    && user.getSaju().ganjiYear() != null
                    && user.getSaju().ganjiMonth() != null
                    && user.getSaju().ganjiDay() != null
                    && user.getSaju().ganjiHour() != null
                ? ChatAdviceRequest.ManseRyok.builder()
                    .year(user.getSaju().ganjiYear())
                    .month(user.getSaju().ganjiMonth())
                    .day(user.getSaju().ganjiDay())
                    .hour(user.getSaju().ganjiHour())
                    .build()
                : null)
        .build();
  }

  private String requestAiAdvice(ChatAdviceRequest request) {
    AiChatAdviceResponse response = chatAdviceClient.getRealtimeAdvice(request);

    if (response == null || response.getData() == null || response.getData().getAdvice() == null) {
      throw new AiServiceException("AI 서버로부터 조언 데이터를 받지 못했습니다.");
    }

    return response.getData().getAdvice();
  }

  private ChatAdvice createNewChatAdvice(String userId, LocalDate today) {
    return ChatAdvice.of(
        null,
        userId,
        DAILY_LIMIT,
        today,
        new ArrayList<>(),
        LocalDateTime.now(clock),
        LocalDateTime.now(clock),
        null);
  }
}
