package com.growit.app.advice.domain.chatadvice.service;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatAdviceService {

  private final ChatAdviceRepository chatAdviceRepository;

  public ChatAdvice getOrCreateChatAdvice(String userId) {
    LocalDate today = LocalDate.now();
    return chatAdviceRepository
        .findByUserId(userId)
        .orElseGet(() -> createNewChatAdvice(userId, today));
  }

  public ChatAdvice resetIfNeeded(ChatAdvice chatAdvice) {
    LocalDate today = LocalDate.now();
    if (!chatAdvice.getLastResetDate().equals(today)) {
      return ChatAdvice.builder()
          .id(chatAdvice.getId())
          .userId(chatAdvice.getUserId())
          .remainingCount(3)
          .lastResetDate(today)
          .conversations(chatAdvice.getConversations())
          .createdAt(chatAdvice.getCreatedAt())
          .updatedAt(LocalDateTime.now())
          .build();
    }
    return chatAdvice;
  }

  private ChatAdvice createNewChatAdvice(String userId, LocalDate today) {
    return ChatAdvice.builder()
        .userId(userId)
        .remainingCount(3)
        .lastResetDate(today)
        .conversations(new ArrayList<>())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}


