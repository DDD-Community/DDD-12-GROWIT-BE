package com.growit.app.user.infrastructure.persistence.useradvicestatus.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_advice_status")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserAdviceStatusEntity extends BaseEntity {

  @Column(name = "user_id", unique = true, nullable = false)
  private String userId;

  @Column(name = "is_goal_onboarding_completed", nullable = false)
  private boolean isGoalOnboardingCompleted;

  public static UserAdviceStatusEntity from(UserAdviceStatus userAdviceStatus) {
    return UserAdviceStatusEntity.builder()
        .userId(userAdviceStatus.getUserId())
        .isGoalOnboardingCompleted(userAdviceStatus.isGoalOnboardingCompleted())
        .build();
  }

  public UserAdviceStatus toDomain() {
    return new UserAdviceStatus(userId, isGoalOnboardingCompleted);
  }

  public void updateByDomain(UserAdviceStatus userAdviceStatus) {
    this.isGoalOnboardingCompleted = userAdviceStatus.isGoalOnboardingCompleted();
  }
}
