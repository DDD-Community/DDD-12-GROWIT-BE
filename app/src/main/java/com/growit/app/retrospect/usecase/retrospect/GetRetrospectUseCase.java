package com.growit.app.retrospect.usecase.retrospect;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.GetRetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetRetrospectUseCase {
  private final RetrospectQuery retrospectQuery;
  private final GoalQuery goalQuery;

  @Transactional(readOnly = true)
  public RetrospectWithPlan execute(GetRetrospectQueryFilter command) {
    Retrospect retrospect = retrospectQuery.getMyRetrospect(command.id(), command.userId());
    Goal goal = goalQuery.getMyGoal(retrospect.getGoalId(), retrospect.getUserId());
    Plan plan = goal.getPlanByPlanId(retrospect.getPlanId());

    return new RetrospectWithPlan(retrospect, plan);
  }
}
