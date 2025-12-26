package com.growit.app.advice.infrastructure.persistence.chatadvice;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.infrastructure.persistence.chatadvice.source.ChatAdviceJpaRepository;
import com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity.ChatAdviceEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatAdviceRepositoryImpl implements ChatAdviceRepository {

  private final ChatAdviceDBMapper mapper;
  private final ChatAdviceJpaRepository chatAdviceJpaRepository;

  @Override
  public Optional<ChatAdvice> findByUserId(String userId) {
    return chatAdviceJpaRepository.findByUserId(userId).map(mapper::toDomain);
  }

  @Override
  public void save(ChatAdvice chatAdvice) {
    Optional<ChatAdviceEntity> existing =
        chatAdviceJpaRepository.findByUserId(chatAdvice.getUserId());
    if (existing.isPresent()) {
      ChatAdviceEntity entity = existing.get();
      mapper.updateEntity(entity, chatAdvice);
      chatAdviceJpaRepository.save(entity);
    } else {
      ChatAdviceEntity entity = mapper.toEntity(chatAdvice);
      chatAdviceJpaRepository.save(entity);
    }
  }

  @Override
  public List<ChatAdvice> findByLastConversatedAtBetween(
      java.time.LocalDateTime start, java.time.LocalDateTime end) {
    List<ChatAdviceEntity> entities =
        chatAdviceJpaRepository.findByLastConversatedAtBetween(start, end);
    return mapper.toDomainList(entities);
  }
}
