package com.growit.app.advice.domain.mentor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MentorAdvice {
  private String id;
  private String userId;
  private String goalId;
  private boolean isChecked;
  private String message;
  private Kpt kpt;
  private LocalDateTime updatedAt; // utc

  public void updateIsChecked(boolean isChecked) {
    this.isChecked = isChecked;
  }

  public boolean shouldFetch() {
    // utc 2025-10-10 15:00:05.007597  현재시간과 차이가 24시간 이상 날때 UTC 기준
    return updatedAt.isBefore(LocalDateTime.now(ZoneOffset.UTC).minusHours(24));
  }

  @Getter
  @AllArgsConstructor
  public static class Kpt {
    private String keep;
    private String problem;
    private String tryNext;
  }
}
