package com.growit.app.advice.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceClient;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceDataCollector;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceService;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import com.growit.app.user.domain.useradvicestatus.repository.UserAdviceStatusRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendChatAdviceUseCaseTest {

  @Mock private ChatAdviceRepository chatAdviceRepository;
  @Mock private ChatAdviceClient chatAdviceClient;
  @Mock private ChatAdviceDataCollector dataCollector;
  @Mock private ChatAdviceService chatAdviceService;
  @Mock private UserAdviceStatusRepository userAdviceStatusRepository;

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
    given(chatAdviceService.getOrCreateChatAdvice(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.resetIfNeeded(chatAdvice)).willReturn(chatAdvice);

    ChatAdviceDataCollector.RealtimeAdviceData data =
        ChatAdviceDataCollector.RealtimeAdviceData.builder()
            .goalId(goalId)
            .selectedGoal("테스트 목표")
            .userMessage(userMessage)
            .recentTodos(List.of("할일1"))
            .build();
    given(dataCollector.collectRealtimeData(user, goalId, userMessage)).willReturn(data);

    AiChatAdviceResponse aiResponse = createAiResponse("격려의 답변입니다.");
    given(chatAdviceClient.getRealtimeAdvice(any(ChatAdviceRequest.class))).willReturn(aiResponse);

    // when
    sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, true);

    // then
    verify(chatAdviceClient)
        .getRealtimeAdvice(
            argThat(
                request ->
                    !request.isGoalOnboardingCompleted()
                        && request.getConcern().equals(userMessage)
                        && request.getGoalId().equals(goalId)));
  }

  @Test
  void givenRemainingCountZero_whenExecute_thenThrowBadRequestException() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(0);
    given(chatAdviceService.getOrCreateChatAdvice(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.resetIfNeeded(chatAdvice)).willReturn(chatAdvice);

    // when & then
    assertThrows(
        BadRequestException.class,
        () -> sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, false));
  }

  @Test
  void givenValidRequest_whenExecute_thenCollectDataWithCorrectParams() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(3);
    given(chatAdviceService.getOrCreateChatAdvice(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.resetIfNeeded(chatAdvice)).willReturn(chatAdvice);

    ChatAdviceDataCollector.RealtimeAdviceData data =
        ChatAdviceDataCollector.RealtimeAdviceData.builder()
            .goalId(goalId)
            .selectedGoal("목표")
            .userMessage(userMessage)
            .recentTodos(new ArrayList<>())
            .build();
    given(dataCollector.collectRealtimeData(user, goalId, userMessage)).willReturn(data);
    given(chatAdviceClient.getRealtimeAdvice(any())).willReturn(createAiResponse("답변"));

    // when
    sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, false);

    // then
    verify(dataCollector).collectRealtimeData(user, goalId, userMessage);
  }

  @Test
  void givenOnboardingRequest_whenExecute_thenUpdateUserAdviceStatus() {
    // given
    ChatAdvice chatAdvice = createChatAdvice(3);
    given(chatAdviceService.getOrCreateChatAdvice(user.getId())).willReturn(chatAdvice);
    given(chatAdviceService.resetIfNeeded(chatAdvice)).willReturn(chatAdvice);

    given(dataCollector.collectRealtimeData(any(), any(), any()))
        .willReturn(
            ChatAdviceDataCollector.RealtimeAdviceData.builder()
                .goalId(goalId)
                .selectedGoal("목표")
                .userMessage(userMessage)
                .recentTodos(new ArrayList<>())
                .build());
    given(chatAdviceClient.getRealtimeAdvice(any())).willReturn(createAiResponse("답변"));

    UserAdviceStatus status = new UserAdviceStatus(user.getId(), false);
    given(userAdviceStatusRepository.findByUserId(user.getId())).willReturn(Optional.of(status));

    // when
    sendChatAdviceUseCase.execute(user, 1, goalId, userMessage, style, true);

    // then
    verify(userAdviceStatusRepository).save(argThat(UserAdviceStatus::isGoalOnboardingCompleted));
  }

  private ChatAdvice createChatAdvice(int remainingCount) {
    return ChatAdvice.builder()
        .userId(user.getId())
        .remainingCount(remainingCount)
        .lastResetDate(LocalDate.now())
        .conversations(new ArrayList<>())
        .build();
  }

  private AiChatAdviceResponse createAiResponse(String advice) {
    AiChatAdviceResponse response = new AiChatAdviceResponse();
    AiChatAdviceResponse.Data data = new AiChatAdviceResponse.Data();
    data.setAdvice(advice);
    response.setData(data);
    return response;
  }
}
