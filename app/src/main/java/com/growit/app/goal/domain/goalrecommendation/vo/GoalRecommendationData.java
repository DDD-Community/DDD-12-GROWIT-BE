package com.growit.app.goal.domain.goalrecommendation.vo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 목표 추천 생성에 필요한 데이터를 담는 값 객체
 */
@Getter
@Builder
public class GoalRecommendationData {
  
  private final List<String> pastTodos;
  private final List<String> completedTodos;
  private final List<String> pastRetrospects;
  private final List<String> pastWeeklyGoals;
  private final String remainingTime;

  /**
   * 빈 회고가 있는지 확인하고 필요시 빈 문자열을 추가합니다.
   * AI 서버의 최소 1개 항목 요구사항을 만족시키기 위함입니다.
   */
  public static class GoalRecommendationDataBuilder {
    public GoalRecommendationDataBuilder pastRetrospects(List<String> pastRetrospects) {
      if (pastRetrospects.isEmpty()) {
        pastRetrospects.add("");
      }
      this.pastRetrospects = pastRetrospects;
      return this;
    }
  }
}
