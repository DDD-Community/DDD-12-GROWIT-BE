package com.growit.app.advice.usecase;

import com.growit.app.advice.controller.dto.response.ChatAdviceResponse;
import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceClient;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceDataCollector;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceService;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.user.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SendChatAdviceUseCase {
  private final ChatAdviceRepository chatAdviceRepository;
  private final ChatAdviceClient chatAdviceClient;
  private final ChatAdviceDataCollector dataCollector;
  private final ChatAdviceService chatAdviceService;

  public ChatAdviceResponse execute(
      User user, Integer week, String goalId, String userMessage, AdviceStyle adviceStyle) {
    String userId = user.getId();

    // 1. ChatAdvice 조회 또는 생성
    ChatAdvice chatAdvice = chatAdviceService.getOrCreateChatAdvice(userId);

    // 2. 일일 초기화 확인
    chatAdvice = chatAdviceService.resetIfNeeded(chatAdvice);

    // 3. Quota 확인
    if (chatAdvice.getRemainingCount() <= 0) {
      throw new BadRequestException("일일 대화 횟수를 초과했습니다.");
    }

    // 4. 데이터 수집
    ChatAdviceDataCollector.RealtimeAdviceData data =
        dataCollector.collectRealtimeData(user, goalId, userMessage);

    ChatAdviceRequest request =
        ChatAdviceRequest.builder()
            .userId(userId)
            .goalId(data.getGoalId())
            .goalTitle(data.getSelectedGoal())
            .concern(data.getUserMessage())
            .mode(adviceStyle.getLabel())
            .recentTodos(data.getRecentTodos())
            .build();

    AiChatAdviceResponse aiResponse =
        chatAdviceClient.getRealtimeAdvice(request);
    
    if (aiResponse == null || aiResponse.getData() == null || aiResponse.getData().getAdvice() == null) {
        throw new RuntimeException("AI 서버로부터 조언 데이터를 받지 못했습니다.");
    }
    
    String grorongResponse = aiResponse.getData().getAdvice();

    // 6. Conversation 추가
    List<ChatAdvice.Conversation> updatedConversations =
        new ArrayList<>(chatAdvice.getConversations());
    updatedConversations.add(
        new ChatAdvice.Conversation(
            week, userMessage, grorongResponse, adviceStyle, LocalDateTime.now()));

    // 7. Quota 차감 및 업데이트
    ChatAdvice updatedChatAdvice =
        ChatAdvice.builder()
            .id(chatAdvice.getId())
            .userId(chatAdvice.getUserId())
            .remainingCount(chatAdvice.getRemainingCount() - 1)
            .lastResetDate(chatAdvice.getLastResetDate())
            .conversations(updatedConversations)
            .createdAt(chatAdvice.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

    // 8. 저장
    chatAdviceRepository.save(updatedChatAdvice);

    // 9. 응답 생성 (해당 주차만 필터링)
    List<ChatAdviceResponse.ConversationResponse> conversations =
        updatedConversations.stream()
            .filter(c -> c.getWeek().equals(week))
            .map(
                c ->
                    new ChatAdviceResponse.ConversationResponse(
                        c.getUserMessage(), c.getGrorongResponse(), c.getTimestamp()))
            .collect(Collectors.toList());

    return ChatAdviceResponse.builder()
        .remainingCount(updatedChatAdvice.getRemainingCount())
        .lastSeenDate(null) // UserAdviceStatus는 별도 관리
        .conversations(conversations)
        .build();
  }
}
