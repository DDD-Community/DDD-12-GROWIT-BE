package com.growit.app.goal.domain.goalretrospect;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goalretrospect.vo.Analysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GoalRetrospect {
  private String id;
  private String goalId;
  private int todoCompletedRate;
  private Analysis analysis;
  private String content;

  public static GoalRetrospect create(
      String goalId, int todoCompletedRate, Analysis analysis, String content) {
    return GoalRetrospect.builder()
        .id(IDGenerator.generateId())
        .goalId(goalId)
        .todoCompletedRate(todoCompletedRate)
        .analysis(analysis)
        .content(content)
        .build();
  }
}