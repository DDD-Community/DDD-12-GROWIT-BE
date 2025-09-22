package com.growit.app.aikpts.infrastructure.persistence.aikpts;

import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import com.growit.app.aikpts.domain.aikpts.AIKPTSRepository;
import com.growit.app.aikpts.infrastructure.persistence.aikpts.source.DBAIKPTSRepository;
import com.growit.app.aikpts.infrastructure.persistence.aikpts.source.entity.AIKPTSEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AIKPTSRepositoryImpl implements AIKPTSRepository {
  
  private final AIKPTSDBMapper mapper;
  private final DBAIKPTSRepository repository;

  @Override
  public void save(AIKPTS aikpts) {
    Optional<AIKPTSEntity> existing = repository.findByUid(aikpts.getId());
    if (existing.isPresent()) {
      AIKPTSEntity entity = existing.get();
      entity.updateByDomain(aikpts);
      repository.save(entity);
    } else {
      AIKPTSEntity entity = mapper.toEntity(aikpts);
      repository.save(entity);
    }
  }

  @Override
  public Optional<AIKPTS> findById(String id) {
    Optional<AIKPTSEntity> entity = repository.findByUid(id);
    return entity.map(mapper::toDomain);
  }

  @Override
  public List<AIKPTS> findByAiAdviceId(String aiAdviceId) {
    List<AIKPTSEntity> entities = repository.findByAiAdviceId(aiAdviceId);
    return mapper.toDomainList(entities);
  }

  @Override
  public boolean existsByAiAdviceId(String aiAdviceId) {
    return repository.findByAiAdviceId(aiAdviceId).stream()
        .anyMatch(entity -> entity.getDeletedAt() == null);
  }

  @Override
  public List<AIKPTS> findActiveByAiAdviceId(String aiAdviceId) {
    List<AIKPTSEntity> entities = repository.findByAiAdviceId(aiAdviceId);
    return entities.stream()
        .filter(entity -> entity.getDeletedAt() == null)
        .map(mapper::toDomain)
        .toList();
  }
}
