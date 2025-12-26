package com.growit.app.advice.batch.writer;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.ChatAdvice.Conversation;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceClient;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class MorningAdviceWriter implements ItemWriter<ChatAdviceRequest> {

  private final ChatAdviceClient chatAdviceClient;
  private final ChatAdviceRepository chatAdviceRepository;

  @Override
  public void write(Chunk<? extends ChatAdviceRequest> chunk) throws Exception {
    for (ChatAdviceRequest request : chunk) {
      try {
        log.debug("Requesting AI advice for user: {}", request.getUserId());
        AiChatAdviceResponse response = chatAdviceClient.getMorningAdvice(request);

        saveAdvice(request.getUserId(), response);

      } catch (Exception e) {
        log.error("Failed to generate/save morning advice for user: {}", request.getUserId(), e);
        // Throwing exception to let Batch handle skip/retry if configured
        throw e;
      }
    }
  }

  private void saveAdvice(String userId, AiChatAdviceResponse response) {
    ChatAdvice chatAdvice =
        chatAdviceRepository
            .findByUserId(userId)
            .orElseThrow(
                () -> new IllegalStateException("User ChatAdvice not found forId: " + userId));

    List<Conversation> conversations = new ArrayList<>(chatAdvice.getConversations());

    // Determine current week from the last valid conversation, or default to 1
    int currentWeek = 1;
    if (chatAdvice.getConversations() != null) {
      for (int i = chatAdvice.getConversations().size() - 1; i >= 0; i--) {
        Conversation c = chatAdvice.getConversations().get(i);
        if (c.getWeek() != null) {
          currentWeek = c.getWeek();
          break;
        }
      }
    }

    conversations.add(
        new Conversation(
            currentWeek,
            "아침 조언 요청",
            response.getData().getAdvice(),
            AdviceStyle.valueOf(response.getData().getMode()),
            LocalDateTime.now(),
            false));

    ChatAdvice updated =
        ChatAdvice.builder()
            .id(chatAdvice.getId())
            .userId(chatAdvice.getUserId())
            .remainingCount(chatAdvice.getRemainingCount())
            .lastResetDate(chatAdvice.getLastResetDate())
            .lastConversatedAt(chatAdvice.getLastConversatedAt())
            .conversations(conversations)
            .createdAt(chatAdvice.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

    chatAdviceRepository.save(updated);
  }
}
