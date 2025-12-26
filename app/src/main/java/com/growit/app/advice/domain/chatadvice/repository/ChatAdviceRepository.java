package com.growit.app.advice.domain.chatadvice.repository;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import java.util.Optional;

public interface ChatAdviceRepository {
  Optional<ChatAdvice> findByUserId(String userId);

  void save(ChatAdvice chatAdvice);

  java.util.List<ChatAdvice> findByLastConversatedAtBetween(
      java.time.LocalDateTime start, java.time.LocalDateTime end);
}
