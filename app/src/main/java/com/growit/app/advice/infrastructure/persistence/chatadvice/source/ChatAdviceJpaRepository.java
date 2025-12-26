package com.growit.app.advice.infrastructure.persistence.chatadvice.source;

import com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity.ChatAdviceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAdviceJpaRepository extends JpaRepository<ChatAdviceEntity, String> {
  Optional<ChatAdviceEntity> findByUserId(String userId);

  java.util.List<ChatAdviceEntity> findByLastConversatedAtBetween(
      java.time.LocalDateTime start, java.time.LocalDateTime end);
}
