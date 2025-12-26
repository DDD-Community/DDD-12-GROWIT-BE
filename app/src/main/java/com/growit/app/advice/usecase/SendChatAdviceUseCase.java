package com.growit.app.advice.usecase;

import com.growit.app.advice.controller.dto.response.ChatAdviceResponse;
import com.growit.app.advice.controller.dto.response.ChatAdviceResponse.ConversationResponse;
import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceService;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.useradvicestatus.service.UserAdviceStatusService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SendChatAdviceUseCase {
  private final ChatAdviceService chatAdviceService;
  private final UserAdviceStatusService userAdviceStatusService;

  public ChatAdviceResponse execute(
      User user,
      Integer week,
      String goalId,
      String userMessage,
      AdviceStyle adviceStyle,
      Boolean isOnboarding) {

    ChatAdvice chatAdvice = chatAdviceService.prepareForNewMessage(user.getId());

    ChatAdvice updatedChatAdvice =
        chatAdviceService.addAdviceConversation(
            chatAdvice, user, week, goalId, userMessage, adviceStyle, isOnboarding);

    if (Boolean.TRUE.equals(isOnboarding)) {
      userAdviceStatusService.completeGoalOnboarding(user.getId());
    }

    return buildResponse(updatedChatAdvice, week, user.getId(), isOnboarding);
  }

  private ChatAdviceResponse buildResponse(
      ChatAdvice chatAdvice, Integer week, String userId, Boolean isOnboarding) {

    List<ConversationResponse> conversations = filterConversationsByWeek(chatAdvice, week);

    boolean isGoalOnboardingCompleted =
        userAdviceStatusService.isGoalOnboardingCompleted(userId, isOnboarding);

    return ChatAdviceResponse.builder()
        .remainingCount(chatAdvice.getRemainingCount())
        .isGoalOnboardingCompleted(isGoalOnboardingCompleted)
        .conversations(conversations)
        .build();
  }

  private List<ConversationResponse> filterConversationsByWeek(
      ChatAdvice chatAdvice, Integer week) {
    return chatAdvice.getConversations().stream()
        .filter(c -> c.getWeek().equals(week))
        .map(
            c ->
                new ConversationResponse(
                    c.getUserMessage(), c.getGrorongResponse(), c.getTimestamp()))
        .collect(Collectors.toList());
  }
}
