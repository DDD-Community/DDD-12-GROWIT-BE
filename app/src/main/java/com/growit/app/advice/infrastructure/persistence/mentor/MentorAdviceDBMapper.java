package com.growit.app.advice.infrastructure.persistence.mentor;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceEntity;
import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceKPTEntity;
import org.springframework.stereotype.Component;

@Component
public class MentorAdviceDBMapper {

  public MentorAdviceEntity toEntity(MentorAdvice mentorAdvice) {
    MentorAdviceEntity entity =
        MentorAdviceEntity.builder()
            .userId(mentorAdvice.getUserId())
            .goalId(mentorAdvice.getGoalId())
            .isChecked(mentorAdvice.isChecked())
            .message(mentorAdvice.getMessage())
            .build();

    if (mentorAdvice.getKpt() != null) {
      entity.kpt = toKptEntity(mentorAdvice.getKpt(), entity);
    }

    return entity;
  }

  public MentorAdvice toDomain(MentorAdviceEntity entity) {
    if (entity == null) return null;

    MentorAdvice.Kpt kptDomain = toKptDomain(entity.getKpt());

    return MentorAdvice.builder()
        .id(entity.getId().toString())
        .userId(entity.getUserId())
        .goalId(entity.getGoalId())
        .isChecked(entity.isChecked())
        .message(entity.getMessage())
        .kpt(kptDomain)
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  private MentorAdviceKPTEntity toKptEntity(
      MentorAdvice.Kpt kpt, MentorAdviceEntity mentorAdviceEntity) {
    if (kpt == null) return null;

    return MentorAdviceKPTEntity.builder()
        .mentorAdvice(mentorAdviceEntity)
        .keep(kpt.getKeep())
        .problem(kpt.getProblem())
        .tryNext(kpt.getTryNext())
        .build();
  }

  private MentorAdvice.Kpt toKptDomain(MentorAdviceKPTEntity entity) {
    if (entity == null) return null;

    return new MentorAdvice.Kpt(entity.getKeep(), entity.getProblem(), entity.getTryNext());
  }
}
