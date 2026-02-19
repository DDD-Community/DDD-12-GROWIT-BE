package com.growit.app.advice.domain.chatadvice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.EarthlyBranchHour;
import com.growit.app.user.domain.user.vo.SajuInfo;
import com.growit.app.user.domain.user.vo.SajuInfo.Gender;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatAdviceServiceSajuTest {

  @Mock private ChatAdviceRepository chatAdviceRepository;
  @Mock private ChatAdviceClient chatAdviceClient;
  @Mock private ChatAdviceDataCollector dataCollector;
  @Mock private Clock clock;

  @InjectMocks private ChatAdviceService chatAdviceService;

  private static final String USER_ID = "user-123";
  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  @Test
  @DisplayName("조언 요청 시 사용자의 사주 정보가 포함되어야 한다")
  void givenUserWithSaju_whenAddAdvice_thenRequestContainsSajuInfo() {
    // given
    given(clock.getZone()).willReturn(ZONE_ID);
    given(clock.instant()).willReturn(java.time.Instant.parse("2024-01-01T00:00:00Z"));

    LocalDate birthDate = LocalDate.of(1990, 1, 1);
    SajuInfo sajuInfo = new SajuInfo(Gender.MALE, birthDate, EarthlyBranchHour.JA, null, null, null, null);
    
    User user = User.builder()
        .id(USER_ID)
        .saju(sajuInfo)
        .build();

    ChatAdvice chatAdvice = ChatAdvice.builder()
        .userId(USER_ID)
        .remainingCount(3)
        .lastResetDate(LocalDate.now(clock))
        .conversations(new ArrayList<>())
        .build();

    ChatAdviceDataCollector.RealtimeAdviceData adviceData = ChatAdviceDataCollector.RealtimeAdviceData.builder()
        .goalId("goal-1")
        .selectedGoal("My Goal")
        .userMessage("My Concern")
        .recentTodos(List.of("Todo 1"))
        .build();
    given(dataCollector.collectRealtimeData(any(), any(), any())).willReturn(adviceData);

    AiChatAdviceResponse.Data aiData = new AiChatAdviceResponse.Data();
    aiData.setAdvice("Response Advice");
    aiData.setMode("SAJU");

    AiChatAdviceResponse aiResponse = new AiChatAdviceResponse();
    aiResponse.setData(aiData);
    aiResponse.setSuccess(true);
    
    given(chatAdviceClient.getRealtimeAdvice(any())).willReturn(aiResponse);

    // when
    chatAdviceService.addAdviceConversation(
        chatAdvice, user, 1, "goal-1", "My verification message", AdviceStyle.SAJU, true
    );

    // then
    ArgumentCaptor<ChatAdviceRequest> captor = ArgumentCaptor.forClass(ChatAdviceRequest.class);
    verify(chatAdviceClient).getRealtimeAdvice(captor.capture());

    ChatAdviceRequest capturedRequest = captor.getValue();
    assertThat(capturedRequest.getBirthDate()).isEqualTo("1990-01-01");
    assertThat(capturedRequest.getBirthTime()).isEqualTo("자시"); // JA label
    assertThat(capturedRequest.getGender()).isEqualTo("남성"); // MALE label
  }
}
