package com.growit.app.goal.domain.planrecommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanRecommendation {
  private String id;
  private String userId;
  private String goalId;
  private String planId;
  private String content;
}
