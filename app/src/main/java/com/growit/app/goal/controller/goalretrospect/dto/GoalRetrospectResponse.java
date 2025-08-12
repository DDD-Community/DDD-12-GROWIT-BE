package com.growit.app.goal.controller.goalretrospect.dto;

import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalRetrospectResponse {
  private String id;
  private String goalId;
  private int todoCompletedRate;
  private Analysis analysis;
  private String content;
}
