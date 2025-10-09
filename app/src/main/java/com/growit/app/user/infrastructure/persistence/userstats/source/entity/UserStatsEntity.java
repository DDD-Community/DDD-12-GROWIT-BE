package com.growit.app.user.infrastructure.persistence.userstats.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.userstats.UserStats;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_stats")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserStatsEntity extends BaseEntity {

  @Column(name = "user_id", unique = true, nullable = false)
  private String userId;

  @Column(name = "last_seen_date", nullable = false)
  private LocalDate lastSeenDate;

  @Column(name = "streak_len", nullable = false)
  private int streakLen;

  public static UserStatsEntity from(UserStats userStats) {
    return UserStatsEntity.builder()
        .userId(userStats.getUserId())
        .lastSeenDate(userStats.getLastSeenDate())
        .streakLen(userStats.getStreakLen())
        .build();
  }

  public UserStats toDomain() {
    return new UserStats(userId, lastSeenDate, streakLen);
  }

  public void updateByDomain(UserStats userStats) {
    this.lastSeenDate = userStats.getLastSeenDate();
    this.streakLen = userStats.getStreakLen();
  }
}
