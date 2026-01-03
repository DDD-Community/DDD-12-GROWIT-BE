package com.growit.app.advice.domain.chatadvice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatAdviceServiceTest {

  @Mock private ChatAdviceRepository chatAdviceRepository;
  @Mock private ChatAdviceClient chatAdviceClient;
  @Mock private ChatAdviceDataCollector dataCollector;
  @Mock private Clock clock; // Mock Clock

  @InjectMocks private ChatAdviceService chatAdviceService;

  private static final String USER_ID = "user-123";
  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    given(clock.getZone()).willReturn(ZONE_ID);
    given(clock.instant())
        .willReturn(java.time.Instant.parse("2024-01-01T00:00:00Z")); // This is 09:00 KST
  }

  @Test
  @DisplayName("사용자가 없으면 새로운 ChatAdvice를 생성한다 - 초기 카운트 3")
  void givenUserNotExists_whenGetOrCreate_thenCreateWithCount3() {
    // given
    LocalDate today = LocalDate.now(clock);
    given(chatAdviceRepository.findByUserId(USER_ID)).willReturn(Optional.empty());

    // when
    ChatAdvice result = chatAdviceService.getOrCreateChatAdvice(USER_ID);

    // then
    assertThat(result.getUserId()).isEqualTo(USER_ID);
    assertThat(result.getRemainingCount()).isEqualTo(3);
    assertThat(result.getLastResetDate()).isEqualTo(today);
    assertThat(result.getConversations()).isEmpty();
  }

  @Test
  @DisplayName("기존 사용자가 있으면 조회한다")
  void givenUserExists_whenGetOrCreate_thenReturnExisting() {
    // given
    ChatAdvice existingAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(2)
            .lastResetDate(LocalDate.now(clock))
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();

    given(chatAdviceRepository.findByUserId(USER_ID)).willReturn(Optional.of(existingAdvice));

    // when
    ChatAdvice result = chatAdviceService.getOrCreateChatAdvice(USER_ID);

    // then
    assertThat(result.getUserId()).isEqualTo(USER_ID);
    assertThat(result.getRemainingCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("오늘 날짜면 리셋하지 않는다")
  void givenSameDay_whenResetIfNeeded_thenNoReset() {
    // given
    LocalDate today = LocalDate.now(clock);
    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(1)
            .lastResetDate(today)
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();

    // when
    ChatAdvice result = chatAdviceService.resetIfNeeded(chatAdvice);

    // then
    assertThat(result.getRemainingCount()).isEqualTo(1);
    assertThat(result.getLastResetDate()).isEqualTo(today);
  }

  @Test
  @DisplayName("날짜가 바뀌면 카운트를 3으로 리셋한다")
  void givenDayChanged_whenResetIfNeeded_thenResetToThree() {
    // given
    LocalDate yesterday = LocalDate.now(clock).minusDays(1);
    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(0)
            .lastResetDate(yesterday)
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();

    // when
    ChatAdvice result = chatAdviceService.resetIfNeeded(chatAdvice);

    // then
    assertThat(result.getRemainingCount()).isEqualTo(3);
    assertThat(result.getLastResetDate()).isEqualTo(LocalDate.now(clock));
  }

  @Test
  @DisplayName("리셋 시 기존 대화 내역은 유지한다")
  void givenDayChanged_whenResetIfNeeded_thenKeepHistory() {
    // given
    LocalDate yesterday = LocalDate.now(clock).minusDays(1);
    ChatAdvice.Conversation conversation =
        new ChatAdvice.Conversation(
            1, null, "test message", "test response", null, LocalDateTime.now(clock), false);

    ArrayList<ChatAdvice.Conversation> conversations = new ArrayList<>();
    conversations.add(conversation);

    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(0)
            .lastResetDate(yesterday)
            .conversations(conversations)
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();

    // when
    ChatAdvice result = chatAdviceService.resetIfNeeded(chatAdvice);

    // then
    assertThat(result.getConversations()).hasSize(1);
    assertThat(result.getConversations().get(0).getUserMessage()).isEqualTo("test message");
  }

  @Test
  @DisplayName("조회 시 날짜가 지났으면 리셋하고 저장한다")
  void givenDayChanged_whenCheckAndReset_thenResetAndSave() {
    // given
    LocalDate yesterday = LocalDate.now(clock).minusDays(1);
    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(0)
            .lastResetDate(yesterday)
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();

    // when
    ChatAdvice result = chatAdviceService.checkAndResetDailyLimit(chatAdvice);

    // then
    assertThat(result.getRemainingCount()).isEqualTo(3);
    assertThat(result.getLastResetDate()).isEqualTo(LocalDate.now(clock));

    // verify save called
    org.mockito.Mockito.verify(chatAdviceRepository, org.mockito.Mockito.times(1))
        .save(org.mockito.ArgumentMatchers.any(ChatAdvice.class));
  }

  @Test
  @DisplayName("조회 시 날짜가 같으면 리셋하지 않고 저장도 안한다")
  void givenSameDay_whenCheckAndReset_thenNoResetNoSave() {
    // given
    LocalDate today = LocalDate.now(clock);
    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(1)
            .lastResetDate(today)
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build();

    // when
    ChatAdvice result = chatAdviceService.checkAndResetDailyLimit(chatAdvice);

    // then
    assertThat(result.getRemainingCount()).isEqualTo(1);
    assertThat(result.getLastResetDate()).isEqualTo(today);

    // verify save NOT called
    org.mockito.Mockito.verify(chatAdviceRepository, org.mockito.Mockito.never())
        .save(org.mockito.ArgumentMatchers.any(ChatAdvice.class));
  }
}
