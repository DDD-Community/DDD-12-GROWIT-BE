package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.BeforeAfterResponse;
import com.growit.app.goal.controller.dto.DurationResponse;
import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.controller.dto.PlanResponse;
import com.growit.app.goal.domain.goal.dto.GoalDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GoalResponseMapper {

  // GoalDto → GoalResponse 오버로드 추가
  public GoalResponse toResponse(GoalDto goalDto) {
    DurationResponse duration =
        DurationResponse.builder()
            .startDate(goalDto.duration().startDate())
            .endDate(goalDto.duration().endDate())
            .build();

    BeforeAfterResponse beforeAfter =
        BeforeAfterResponse.builder()
            .asIs(goalDto.beforeAfter().asIs())
            .toBe(goalDto.beforeAfter().toBe())
            .build();

    List<PlanResponse> plans =
        goalDto.plans().stream()
            .map(plan -> PlanResponse.builder().id(plan.getId()).content(plan.getContent()).build())
            .collect(Collectors.toList());

    return GoalResponse.builder()
        .id(goalDto.id())
        .name(goalDto.name())
        .duration(duration)
        .beforeAfter(beforeAfter)
        .plans(plans)
        .build();
  }
}
