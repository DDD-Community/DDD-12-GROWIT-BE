package com.growit.app.ai.infrastructure.persistence.aiadvice;

import com.growit.app.ai.domain.aiadvice.AIAdvice;
import com.growit.app.ai.infrastructure.persistence.aiadvice.source.entity.AIAdviceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AIAdviceDBMapper {

  public AIAdviceEntity toEntity(AIAdvice aiAdvice) {
    return AIAdviceEntity.builder()
        .uid(aiAdvice.getId())
        .userId(aiAdvice.getUserId())
        .goalId(aiAdvice.getGoalId())
        .promptId(aiAdvice.getPromptId())
        .templateUid(aiAdvice.getTemplateUid())
        .success(aiAdvice.getSuccess())
        .errorMessage(aiAdvice.getErrorMessage())
        .adviceContent(aiAdvice.getAdviceContent())
        .build();
  }

  public AIAdvice toDomain(AIAdviceEntity entity) {
    return AIAdvice.builder()
        .id(entity.getUid())
        .userId(entity.getUserId())
        .goalId(entity.getGoalId())
        .promptId(entity.getPromptId())
        .templateUid(entity.getTemplateUid())
        .success(entity.getSuccess())
        .errorMessage(entity.getErrorMessage())
        .adviceContent(entity.getAdviceContent())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .isDeleted(entity.getDeletedAt() != null)
        .build();
  }

  public List<AIAdvice> toDomainList(List<AIAdviceEntity> entities) {
    return entities.stream()
        .map(this::toDomain)
        .toList();
  }
}
