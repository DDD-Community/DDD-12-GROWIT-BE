package com.growit.app.aikpts.infrastructure.persistence.aikpts;

import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import com.growit.app.aikpts.domain.aikpts.vo.KPT;
import com.growit.app.aikpts.infrastructure.persistence.aikpts.source.entity.AIKPTSEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AIKPTSDBMapper {

  public AIKPTSEntity toEntity(AIKPTS aikpts) {
    return AIKPTSEntity.builder()
        .uid(aikpts.getId())
        .aiAdviceId(aikpts.getAiAdviceId())
        .keeps(String.join("|", aikpts.getKpt().getKeeps()))
        .problems(String.join("|", aikpts.getKpt().getProblems()))
        .trys(String.join("|", aikpts.getKpt().getTrys()))
        .build();
  }

  public AIKPTS toDomain(AIKPTSEntity entity) {
    KPT kpt = KPT.builder()
        .keeps(parseStoredText(entity.getKeeps()))
        .problems(parseStoredText(entity.getProblems()))
        .trys(parseStoredText(entity.getTrys()))
        .build();

    return AIKPTS.builder()
        .id(entity.getUid())
        .aiAdviceId(entity.getAiAdviceId())
        .kpt(kpt)
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .isDeleted(entity.getDeletedAt() != null)
        .build();
  }

  private List<String> parseStoredText(String text) {
    if (text == null || text.trim().isEmpty()) {
      return List.of();
    }
    return Arrays.stream(text.split("\\|"))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());
  }

  public List<AIKPTS> toDomainList(List<AIKPTSEntity> entities) {
    return entities.stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }
}
