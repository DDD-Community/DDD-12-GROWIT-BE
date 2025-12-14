package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationDataCollector;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationService;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendPlanUseCase {

  private final GoalRecommendationDataCollector dataCollector;
  private final GoalRecommendationService recommendationService;
  private final GetGoalUseCase getGoalUseCase;

  public void execute(User user, String goalId, String planId) {
    Goal currentGoal = getGoalUseCase.getGoal(goalId, user);
    GoalRecommendationData data = dataCollector.collectData(user, currentGoal);

    recommendationService.generateRecommendation(user, currentGoal, data);
  }
}
