package com.growit.app.user.infrastructure.persistence.useradvicestatus.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
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

  @Column(name = "last_seen_date")
  private LocalDate lastSeenDate;

  public static UserAdviceStatusEntity from(UserAdviceStatus userAdviceStatus) {
    return UserAdviceStatusEntity.builder()
        .userId(userAdviceStatus.getUserId())
        .lastSeenDate(userAdviceStatus.getLastSeenDate())
        .build();
  }

  public UserAdviceStatus toDomain() {
    return new UserAdviceStatus(userId, lastSeenDate);
  }

  public void updateByDomain(UserAdviceStatus userAdviceStatus) {
    this.lastSeenDate = userAdviceStatus.getLastSeenDate();
  }
}
