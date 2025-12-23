package com.growit.app.advice.infrastructure.persistence.chatadvice;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.infrastructure.persistence.chatadvice.source.ChatAdviceJpaRepository;
import com.growit.app.advice.infrastructure.persistence.chatadvice.source.entity.ChatAdviceEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatAdviceRepositoryImpl implements ChatAdviceRepository {

  private final ChatAdviceJpaRepository chatAdviceJpaRepository;

  @Override
  public Optional<ChatAdvice> findByUserId(String userId) {
    return chatAdviceJpaRepository.findByUserId(userId).map(ChatAdviceEntity::toDomain);
  }

  @Override
  public void save(ChatAdvice chatAdvice) {
    chatAdviceJpaRepository
        .findByUserId(chatAdvice.getUserId())
        .ifPresentOrElse(
            entity -> {
              entity.updateByDomain(chatAdvice);
              chatAdviceJpaRepository.save(entity);
            },
            () -> chatAdviceJpaRepository.save(ChatAdviceEntity.from(chatAdvice)));
  }
}
