package com.growit.app.advice.domain.chatadvice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

  @InjectMocks private ChatAdviceService chatAdviceService;

  private static final String USER_ID = "user-123";

  @Test
  @DisplayName("사용자가 없으면 새로운 ChatAdvice를 생성한다 - 초기 카운트 3")
  void givenUserNotExists_whenGetOrCreate_thenCreateWithCount3() {
    // given
    given(chatAdviceRepository.findByUserId(USER_ID)).willReturn(Optional.empty());

    // when
    ChatAdvice result = chatAdviceService.getOrCreateChatAdvice(USER_ID);

    // then
    assertThat(result.getUserId()).isEqualTo(USER_ID);
    assertThat(result.getRemainingCount()).isEqualTo(3);
    assertThat(result.getLastResetDate()).isEqualTo(LocalDate.now());
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
            .lastResetDate(LocalDate.now())
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
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
    LocalDate today = LocalDate.now();
    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(1)
            .lastResetDate(today)
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
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
    LocalDate yesterday = LocalDate.now().minusDays(1);
    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(0)
            .lastResetDate(yesterday)
            .conversations(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    // when
    ChatAdvice result = chatAdviceService.resetIfNeeded(chatAdvice);

    // then
    assertThat(result.getRemainingCount()).isEqualTo(3);
    assertThat(result.getLastResetDate()).isEqualTo(LocalDate.now());
  }

  @Test
  @DisplayName("리셋 시 기존 대화 내역은 유지한다")
  void givenDayChanged_whenResetIfNeeded_thenKeepHistory() {
    // given
    LocalDate yesterday = LocalDate.now().minusDays(1);
    ChatAdvice.Conversation conversation =
        new ChatAdvice.Conversation(1, "test message", "test response", null, LocalDateTime.now(), false);

    ArrayList<ChatAdvice.Conversation> conversations = new ArrayList<>();
    conversations.add(conversation);

    ChatAdvice chatAdvice =
        ChatAdvice.builder()
            .userId(USER_ID)
            .remainingCount(0)
            .lastResetDate(yesterday)
            .conversations(conversations)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    // when
    ChatAdvice result = chatAdviceService.resetIfNeeded(chatAdvice);

    // then
    assertThat(result.getConversations()).hasSize(1);
    assertThat(result.getConversations().get(0).getUserMessage()).isEqualTo("test message");
  }
}
