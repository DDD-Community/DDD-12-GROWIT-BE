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
  private boolean isChecked;

  @Column(name = "message", nullable = false, columnDefinition = "TEXT")
  private String message;

  @OneToOne(mappedBy = "mentorAdvice", cascade = CascadeType.ALL)
  public MentorAdviceKPTEntity kpt;

  public void updateByDomain(MentorAdvice mentorAdvice) {
    this.isChecked = mentorAdvice.isChecked();
    this.message = mentorAdvice.getMessage();

    if (this.kpt != null && mentorAdvice.getKpt() != null) {
      this.kpt.updateByDomain(mentorAdvice.getKpt());
    }
  }
}
