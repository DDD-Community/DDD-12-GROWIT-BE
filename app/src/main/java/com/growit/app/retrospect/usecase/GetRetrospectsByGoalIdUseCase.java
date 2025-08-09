package com.growit.app.retrospect.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.dto.RetrospectWithPlan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetRetrospectsByGoalIdUseCase {
  private final RetrospectQuery retrospectQuery;
  private final GoalQuery goalQuery;

  @Transactional(readOnly = true)
  public List<RetrospectWithPlan> execute(String goalId, String userId) {
    final List<Retrospect> retrospects = retrospectQuery.getRetrospectsByGoalId(goalId, userId);
    final Goal goal = goalQuery.getMyGoal(goalId, userId);

    return retrospects.stream()
        .map(
            retrospect -> {
              final Plan plan = goal.getPlanByPlanId(retrospect.getPlanId());
              return new RetrospectWithPlan(retrospect, plan);
            })
        .toList();
  }
}