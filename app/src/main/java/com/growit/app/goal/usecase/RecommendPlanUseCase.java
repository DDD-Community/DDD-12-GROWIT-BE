package com.growit.app.goal.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendationRepository;
import com.growit.app.goal.domain.planrecommendation.dto.FindPlanRecommendationCommand;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendPlanUseCase {

  private final PlanRecommendationRepository planRecommendationRepository;
  private final GetUserGoalsUseCase getUserGoalsUseCase;

  public PlanRecommendation execute(User user, String planId) {
    // 1. 현재 실행중인 목표가 있는지 확인
    Goal currentGoal =
        getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("현재 진행중인 목표가 존재하지 않습니다."));

    Plan plan = currentGoal.getPlanByPlanId(planId);

    // 2. 기존 추천이 있는지 확인하고 업데이트하거나 새로 생성
    FindPlanRecommendationCommand command =
        new FindPlanRecommendationCommand(user.getId(), currentGoal.getId(), plan.getId());

    return planRecommendationRepository.findByCommand(command).orElse(null);
  }
}
