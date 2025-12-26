package com.growit.app.advice.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceService;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.useradvicestatus.service.UserAdviceStatusService;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendChatAdviceUseCaseTest {

  @Mock private ChatAdviceService chatAdviceService;
  @Mock private UserAdviceStatusService userAdviceStatusService;

  @InjectMocks private SendChatAdviceUseCase sendChatAdviceUseCase;

  private User user;
  private final String goalId = "goal-123";
  private final String userMessage = "오늘 너무 힘들어요";
  private final AdviceStyle style = AdviceStyle.WARM;

  @BeforeEach
  void setUp() {
    user = UserFixture.defaultUser();
  }

  @Test
  void givenOnboardingRequest_whenExecute_thenIncludeOnboardingFlagInAiRequest() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(3);
    ChatAdvice updatedChatAdvice = createChatAdvice(2);

    given(chatAdviceService.prepareForNewMessage(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.addAdviceConversation(
        chatAdvice, user, 1, goalId, userMessage, style, true))
        .willReturn(updatedChatAdvice);
    given(userAdviceStatusService.isGoalOnboardingCompleted(user.getId(), true)).willReturn(false);

    // when
    sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, true);

    // then
    verify(userAdviceStatusService).completeGoalOnboarding(user.getId());
  }

  @Test
  void givenRemainingCountZero_whenExecute_thenThrowBadRequestException() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(0);
    given(chatAdviceService.prepareForNewMessage(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.addAdviceConversation(
        chatAdvice, user, 1, goalId, userMessage, style, false))
        .willThrow(new BadRequestException("ADVICE_COUNT_EXHAUSTED"));

    // when & then
    assertThrows(
        BadRequestException.class,
        () -> sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, false));
  }

  @Test
  void givenValidRequest_whenExecute_thenCollectDataWithCorrectParams() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(3);
    ChatAdvice updatedChatAdvice = createChatAdvice(2);

    given(chatAdviceService.prepareForNewMessage(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.addAdviceConversation(
        chatAdvice, user, 1, goalId, userMessage, style, false))
        .willReturn(updatedChatAdvice);
    given(userAdviceStatusService.isGoalOnboardingCompleted(user.getId(), false)).willReturn(true);

    // when
    sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, false);

    // then
    verify(chatAdviceService).addAdviceConversation(chatAdvice, user, 1, goalId, userMessage, style, false);
  }

  @Test
  void givenOnboardingRequest_whenExecute_thenUpdateUserAdviceStatus() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(3);
    ChatAdvice updatedChatAdvice = createChatAdvice(2);

    given(chatAdviceService.prepareForNewMessage(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.addAdviceConversation(
        chatAdvice, user, 1, goalId, userMessage, style, true))
        .willReturn(updatedChatAdvice);
    given(userAdviceStatusService.isGoalOnboardingCompleted(user.getId(), true)).willReturn(true);

    // when
    sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, true);

    // then
    verify(userAdviceStatusService).completeGoalOnboarding(user.getId());
  }

  private ChatAdvice createChatAdvice(int remainingCount) {
    return ChatAdvice.builder()
        .userId(user.getId())
        .remainingCount(remainingCount)
        .lastResetDate(LocalDate.now())
        .conversations(new ArrayList<>())
        .createdAt(java.time.LocalDateTime.now())
        .updatedAt(java.time.LocalDateTime.now())
        .build();
  }

  @Test
  void givenValidRequest_whenExecute_thenDecreaseRemainingCount() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(3);
    ChatAdvice updatedChatAdvice = createChatAdvice(2);

    given(chatAdviceService.prepareForNewMessage(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.addAdviceConversation(
        chatAdvice, user, 1, goalId, userMessage, style, false))
        .willReturn(updatedChatAdvice);
    given(userAdviceStatusService.isGoalOnboardingCompleted(user.getId(), false)).willReturn(true);

    // when
    var result = sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, false);

    // then
    verify(chatAdviceService).addAdviceConversation(chatAdvice, user, 1, goalId, userMessage, style, false);
  }

  @Test
  void givenValidRequest_whenExecute_thenSaveConversation() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(3);
    ChatAdvice updatedChatAdvice = createChatAdvice(2);

    given(chatAdviceService.prepareForNewMessage(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.addAdviceConversation(
        chatAdvice, user, 1, goalId, userMessage, style, false))
        .willReturn(updatedChatAdvice);
    given(userAdviceStatusService.isGoalOnboardingCompleted(user.getId(), false)).willReturn(true);

    // when
    sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, false);

    // then
    verify(chatAdviceService).addAdviceConversation(chatAdvice, user, 1, goalId, userMessage, style, false);
  }
}
