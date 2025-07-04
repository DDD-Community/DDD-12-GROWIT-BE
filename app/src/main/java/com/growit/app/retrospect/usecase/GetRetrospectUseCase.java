package com.growit.app.retrospect.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.command.GetRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectWithPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetRetrospectUseCase {
  private final RetrospectRepository retrospectRepository;
  private final GoalRepository goalRepository;

  @Transactional(readOnly = true)
  public RetrospectWithPlan execute(GetRetrospectCommand command) {
    Retrospect retrospect =
        retrospectRepository
            .findById(command.id())
            .orElseThrow(() -> new NotFoundException("회고를 찾을 수 없습니다."));

    Goal goal =
        goalRepository
            .findById(retrospect.getGoalId())
            .orElseThrow(() -> new NotFoundException("목표가 존재하지 않습니다."));

    Plan plan =
        goal.getPlans().stream()
            .filter(p -> p.getId().equals(retrospect.getPlanId()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("일치하는 Plan이 없습니다."));

    return new RetrospectWithPlan(retrospect, plan);
  }
}
