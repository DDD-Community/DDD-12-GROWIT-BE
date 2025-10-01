package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationDataCollector;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationService;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendationRepository;
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
  private final PlanRecommendationRepository planRecommendationRepository;
  private final GetGoalUseCase getGoalUseCase;

  public PlanRecommendation execute(User user, String goalId, String planId) {
    Goal currentGoal = getGoalUseCase.getGoal(goalId, user);
    Plan plan = currentGoal.getPlanByPlanId(planId);
    GoalRecommendationData data = dataCollector.collectData(user, currentGoal);

    return recommendationService.generateRecommendation(user, currentGoal, data);
//    // 2. 기존 추천이 있는지 확인하고 업데이트하거나 새로 생성
//    FindPlanRecommendationCommand command =
//        new FindPlanRecommendationCommand(user.getId(), currentGoal.getId(), plan.getId());

//    return planRecommendationRepository
//        .findByCommand(command)
//        .orElse(
//            new PlanRecommendation(
//                IDGenerator.generateId(),
//                user.getId(),
//                currentGoal.getId(),
//                planId,
//                "당신은 목표를 추천합니다." // ai 연동전에만 내려드리도록 하겠습니다.
//                ));
  }
}
