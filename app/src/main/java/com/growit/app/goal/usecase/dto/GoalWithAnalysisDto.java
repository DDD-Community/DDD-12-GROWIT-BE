package com.growit.app.goal.usecase.dto;

import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.domain.goal.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalWithAnalysisDto {
  private final Goal goal;
  private final GoalAnalysis analysis;
}
