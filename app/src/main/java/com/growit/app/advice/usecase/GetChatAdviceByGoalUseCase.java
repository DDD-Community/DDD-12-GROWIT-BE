package com.growit.app.advice.usecase;

import com.growit.app.advice.controller.dto.response.ChatAdviceResponse;
import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import com.growit.app.user.domain.useradvicestatus.repository.UserAdviceStatusRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetChatAdviceByGoalUseCase {
  private final UserAdviceStatusRepository userAdviceStatusRepository;
  private final ChatAdviceRepository chatAdviceRepository;

  public ChatAdviceResponse execute(User user, Integer week, String goalId) {
    String userId = user.getId();

    // 1. ChatAdvice 조회
    ChatAdvice chatAdvice = chatAdviceRepository.findByUserId(userId).orElse(null);

    // 2. 처음 본 날짜 조회 및 초기화
    UserAdviceStatus userAdviceStatus =
        userAdviceStatusRepository
            .findByUserId(userId)
            .orElseGet(
                () -> {
                  UserAdviceStatus newStatus = new UserAdviceStatus(userId, false);
                  userAdviceStatusRepository.save(newStatus);
                  return newStatus;
                });

    // 3. 남은 대화 횟수 조회
    int remainingCount = chatAdvice != null ? chatAdvice.getRemainingCount() : 3;

    // 4. 해당 목표(goalId) 및 주차(week)의 대화 내역 필터링
    List<ChatAdviceResponse.ConversationResponse> conversations =
        chatAdvice != null && chatAdvice.getConversations() != null
            ? chatAdvice.getConversations().stream()
                .filter(c -> goalId.equals(c.getGoalId()))
                .filter(c -> week == null || (c.getWeek() != null && c.getWeek().equals(week)))
                .map(
                    c ->
                        new ChatAdviceResponse.ConversationResponse(
                            c.getUserMessage(), c.getGrorongResponse(), c.getTimestamp()))
                .collect(Collectors.toList())
            : Collections.emptyList();

    // 5. 응답 반환
    return ChatAdviceResponse.builder()
        .remainingCount(remainingCount)
        .isGoalOnboardingCompleted(userAdviceStatus.isGoalOnboardingCompleted())
        .conversations(conversations)
        .build();
  }
}
