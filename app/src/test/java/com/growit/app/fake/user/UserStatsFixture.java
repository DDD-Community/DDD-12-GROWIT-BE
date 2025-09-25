package com.growit.app.fake.user;

import com.growit.app.user.domain.userstats.UserStats;
import java.time.LocalDate;

public class UserStatsFixture {

  public static UserStats defaultUserStats() {
    return new UserStatsBuilder().build();
  }

  public static UserStats threeDayStreakUserStats() {
    return new UserStatsBuilder().withStreakLen(3).withLastSeenDate(LocalDate.now()).build();
  }

  public static UserStats notAccessedForThreeDaysUserStats() {
    return new UserStatsBuilder()
        .withStreakLen(1)
        .withLastSeenDate(LocalDate.now().minusDays(4))
        .build();
  }

  public static UserStats normalUserStats() {
    return new UserStatsBuilder()
        .withStreakLen(2)
        .withLastSeenDate(LocalDate.now().minusDays(1))
        .build();
  }
}

class UserStatsBuilder {
  private String userId = "user-1";
  private LocalDate lastSeenDate = LocalDate.now().minusDays(1);
  private int streakLen = 1;

  public UserStatsBuilder withUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public UserStatsBuilder withLastSeenDate(LocalDate lastSeenDate) {
    this.lastSeenDate = lastSeenDate;
    return this;
  }

  public UserStatsBuilder withStreakLen(int streakLen) {
    this.streakLen = streakLen;
    return this;
  }

  public UserStats build() {
    return new UserStats(userId, lastSeenDate, streakLen);
  }
}
