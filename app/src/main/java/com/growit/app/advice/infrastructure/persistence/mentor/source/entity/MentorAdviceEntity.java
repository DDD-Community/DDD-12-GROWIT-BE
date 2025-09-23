package com.growit.app.advice.infrastructure.persistence.mentor.source.entity;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "mentor_advice",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "goal_id"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MentorAdviceEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false, length = 36)
  private String userId;

  @Column(name = "goal_id", nullable = false, length = 36)
  private String goalId;

  @Column(name = "is_checked", nullable = false)
  private Boolean isChecked = false;

  @Column(name = "message", nullable = false, columnDefinition = "TEXT")
  private String message;

  @OneToOne(mappedBy = "mentorAdvice", cascade = CascadeType.ALL)
  private MentorAdviceKPTEntity kpt;

  public static MentorAdviceEntity from(MentorAdvice mentorAdvice) {
    MentorAdviceEntity entity =
        MentorAdviceEntity.builder()
            .userId(mentorAdvice.getUserId())
            .goalId(mentorAdvice.getGoalId())
            .isChecked(mentorAdvice.isChecked())
            .message(mentorAdvice.getMessage())
            .build();

    entity.kpt = MentorAdviceKPTEntity.from(mentorAdvice.getKpt(), entity);

    return entity;
  }

  public MentorAdvice toDomain() {
    MentorAdvice.Kpt kptDomain = kpt != null ? kpt.toDomain() : null;
    return new MentorAdvice(getId().toString(), userId, goalId, isChecked, message, kptDomain);
  }

  public void updateByDomain(MentorAdvice mentorAdvice) {
    this.isChecked = mentorAdvice.isChecked();
    this.message = mentorAdvice.getMessage();

    if (this.kpt != null && mentorAdvice.getKpt() != null) {
      this.kpt.updateByDomain(mentorAdvice.getKpt());
    }
  }
}
