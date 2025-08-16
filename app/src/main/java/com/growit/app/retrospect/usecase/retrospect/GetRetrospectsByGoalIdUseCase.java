package com.growit.app.retrospect.usecase.retrospect;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    final Goal goal = goalQuery.getMyGoal(goalId, userId);
    final Map<String, Retrospect> retrospects =
        retrospectQuery.getRetrospectsByGoalId(goalId, userId).stream()
            .collect(Collectors.toMap(Retrospect::getPlanId, r -> r));

    return goal.getPlans().stream()
        .map(plan -> new RetrospectWithPlan(retrospects.get(plan.getId()), plan))
        .toList();
  }
}
