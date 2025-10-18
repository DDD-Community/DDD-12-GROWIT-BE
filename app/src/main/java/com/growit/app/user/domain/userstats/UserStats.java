package com.growit.app.user.domain.userstats;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserStats {
  private String userId;
  private LocalDate lastSeenDate;
  private int streakLen;

  public void updateLastSeenDate(LocalDate currentDate) {
    if (lastSeenDate == null) {
      this.lastSeenDate = currentDate;
      this.streakLen = 1;
      return;
    }

    long daysBetween = ChronoUnit.DAYS.between(lastSeenDate, currentDate);

    if (daysBetween == 1) {
      // 연속 출석
      this.streakLen++;
    } else if (daysBetween > 1) {
      // 연속성 끊어짐
      this.streakLen = 1;
    }
    // daysBetween == 0 이면 같은 날이므로 업데이트 안함

    this.lastSeenDate = currentDate;
  }

  private boolean hasThreeDayStreak() {
    return streakLen >= 3;
  }

  private boolean hasNotAccessedForThreeDays(LocalDate currentDate) {
    if (lastSeenDate == null) {
      return false;
    }
    return ChronoUnit.DAYS.between(lastSeenDate, currentDate) >= 3;
  }

  public boolean isActiveUser(int diffDay) {
    return isActiveUser(LocalDate.now(), diffDay);
  }

  public boolean isActiveUser(LocalDate currentDate, int diffDay) {
    if (lastSeenDate == null) {
      return false;
    }

    long daysBetween = ChronoUnit.DAYS.between(lastSeenDate, currentDate);
    return daysBetween <= diffDay;
  }

  public AccessStatus getAccessStatus(LocalDate currentDate) {
    if (hasThreeDayStreak()) {
      return AccessStatus.THREE_DAYS_OR_MORE;
    }
    if (hasNotAccessedForThreeDays(currentDate)) {
      return AccessStatus.NOT_ACCESSED_FOR_THREE_DAYS;
    }
    return AccessStatus.DEFAULT;
  }

  public enum AccessStatus {
    THREE_DAYS_OR_MORE, // 3일 이상 접속
    DEFAULT, // 디폴트
    NOT_ACCESSED_FOR_THREE_DAYS // 3일 이상 접속 안함
  }
}
