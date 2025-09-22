package com.growit.app.user.infrastructure.persistence.userstats.source.entity;

import com.growit.app.user.domain.userstats.UserStats;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_status")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsEntity {

    @Id
    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(name = "last_seen_date", nullable = false)
    private LocalDate lastSeenDate;

    @Column(name = "streak_len", nullable = false)
    @Builder.Default
    private Integer streakLen = 0;

    public static UserStatsEntity from(UserStats userStats) {
        return UserStatsEntity.builder()
                .userId(userStats.userId())
                .lastSeenDate(userStats.lastSeenDate())
                .streakLen(userStats.streakLen())
                .build();
    }

    public UserStats toDomain() {
        return new UserStats(userId, lastSeenDate, streakLen);
    }

    public void updateByDomain(UserStats userStats) {
        this.lastSeenDate = userStats.lastSeenDate();
        this.streakLen = userStats.streakLen();
    }
}
