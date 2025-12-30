package com.growit.app.advice.infrastructure.persistence.chatadvice.source;

import com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity.ChatAdviceEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAdviceJpaRepository extends JpaRepository<ChatAdviceEntity, String> {
  Optional<ChatAdviceEntity> findByUserId(String userId);

  List<ChatAdviceEntity> findByLastConversatedAtBetween(
      LocalDateTime start, LocalDateTime end);
}
