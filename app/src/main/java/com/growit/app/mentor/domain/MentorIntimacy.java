package com.growit.app.mentor.domain;

import com.growit.app.mentor.domain.vo.IntimacyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MentorIntimacy {
  private String userId;
  private IntimacyLevel level;
  private int totalTodoCount;
  private int weeklyRetrospectCount;

  public static IntimacyLevel calculateLevel(int totalTodoCount, int weeklyRetrospectCount) {
    return determineLevel(totalTodoCount, weeklyRetrospectCount);
  }

  private static IntimacyLevel determineLevel(int totalTodoCount, int weeklyRetrospectCount) {
    // 상: 전체 투두 50개 이상, 주간 회고 2개 이상
    if (totalTodoCount >= 50 && weeklyRetrospectCount >= 2) {
      return IntimacyLevel.HIGH;
    }

    // 하: 전체 투두 20개 이하, 주간 회고 1개 이하
    if (totalTodoCount <= 20 && weeklyRetrospectCount <= 1) {
      return IntimacyLevel.LOW;
    }

    // 중: 디폴트 (나머지)
    return IntimacyLevel.MEDIUM;
  }
}
