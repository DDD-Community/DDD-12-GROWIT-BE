package com.growit.app.ai.infrastructure.persistence.aiadvice;

import com.growit.app.ai.domain.aiadvice.AIAdvice;
import com.growit.app.ai.domain.aiadvice.AIAdviceRepository;
import com.growit.app.ai.infrastructure.persistence.aiadvice.source.DBAIAdviceRepository;
import com.growit.app.ai.infrastructure.persistence.aiadvice.source.entity.AIAdviceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AIAdviceRepositoryImpl implements AIAdviceRepository {
  
  private final DBAIAdviceRepository repository;
  private final AIAdviceDBMapper mapper;

  @Override
  public void save(AIAdvice aiAdvice) {
    // 기존 레코드가 있는지 확인
    Optional<AIAdviceEntity> existingEntity = repository.findByUid(aiAdvice.getId());
    
    if (existingEntity.isPresent()) {
      // 기존 레코드 업데이트
      AIAdviceEntity entity = existingEntity.get();
      entity.setUserId(aiAdvice.getUserId());
      entity.setGoalId(aiAdvice.getGoalId());
      entity.setPromptId(aiAdvice.getPromptId());
      entity.setTemplateUid(aiAdvice.getTemplateUid());
      entity.setSuccess(aiAdvice.getSuccess());
      entity.setErrorMessage(aiAdvice.getErrorMessage());
      entity.setAdviceContent(aiAdvice.getAdviceContent());
      entity.setUpdatedAt(aiAdvice.getUpdatedAt());
      
      repository.save(entity);
    } else {
      // 새로운 레코드 생성
      AIAdviceEntity entity = mapper.toEntity(aiAdvice);
      repository.save(entity);
    }
  }

  @Override
  public Optional<AIAdvice> findById(String id) {
    Optional<AIAdviceEntity> entity = repository.findByUid(id);
    return entity.map(mapper::toDomain);
  }

  @Override
  public List<AIAdvice> findByUserId(String userId) {
    List<AIAdviceEntity> entities = repository.findByUserIdOrderByCreatedAtDesc(userId);
    return mapper.toDomainList(entities);
  }

  @Override
  public List<AIAdvice> findByUserIdAndGoalId(String userId, String goalId) {
    List<AIAdviceEntity> entities = repository.findByUserIdAndGoalIdOrderByCreatedAtDesc(userId, goalId);
    return mapper.toDomainList(entities);
  }

  @Override
  public List<AIAdvice> findRecentByUserId(String userId, int limit) {
    List<AIAdviceEntity> entities = repository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
    return mapper.toDomainList(entities);
  }

  @Override
  public boolean existsByUserIdAndDate(String userId, LocalDate date) {
    return repository.existsByUserIdAndCreatedAtBetween(userId, 
        date.atStartOfDay(), 
        date.plusDays(1).atStartOfDay());
  }

  @Override
  public Optional<AIAdvice> findByUserIdAndDate(String userId, LocalDate date) {
    Optional<AIAdviceEntity> entity = repository.findByUserIdAndCreatedAtBetween(userId,
        date.atStartOfDay(),
        date.plusDays(1).atStartOfDay());
    return entity.map(mapper::toDomain);
  }

  @Override
  public boolean existsByUserIdAndGoalIdAndDate(String userId, String goalId, LocalDate date) {
    return repository.existsByUserIdAndGoalIdAndCreatedAtBetween(userId, goalId,
        date.atStartOfDay(),
        date.plusDays(1).atStartOfDay());
  }

  @Override
  public Optional<AIAdvice> findByUserIdAndGoalIdAndDate(String userId, String goalId, LocalDate date) {
    Optional<AIAdviceEntity> entity = repository.findByUserIdAndGoalIdAndCreatedAtBetween(userId, goalId,
        date.atStartOfDay(),
        date.plusDays(1).atStartOfDay());
    return entity.map(mapper::toDomain);
  }
}
