package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.BeforeAfterResponse;
import com.growit.app.goal.controller.dto.DurationResponse;
import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.controller.dto.PlanResponse;
import com.growit.app.goal.domain.goal.Goal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GoalResponseMapper {

  public GoalResponse toResponse(Goal goal) {
    // Duration
    DurationResponse duration =
        DurationResponse.builder()
            .startDate(goal.getDuration().startDate())
            .endDate(goal.getDuration().endDate())
            .build();

    // BeforeAfter
    BeforeAfterResponse beforeAfter =
        BeforeAfterResponse.builder()
            .asIs(goal.getBeforeAfter().asIs())
            .toBe(goal.getBeforeAfter().toBe())
            .build();

    // Plans
    List<PlanResponse> plans =
        goal.getPlans().stream()
            .map(plan -> PlanResponse.builder().id(plan.getId()).content(plan.getContent()).build())
            .collect(Collectors.toList());

    return GoalResponse.builder()
        .id(goal.getId())
        .name(goal.getName())
        .duration(duration)
        .beforeAfter(beforeAfter)
        .plans(plans)
        .build();
  }
}
