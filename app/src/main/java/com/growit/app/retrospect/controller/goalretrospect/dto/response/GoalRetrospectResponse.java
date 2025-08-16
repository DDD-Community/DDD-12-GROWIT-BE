package com.growit.app.retrospect.controller.goalretrospect.dto.response;

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
