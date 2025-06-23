package com.growit.app.goal.domain.goal;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Goal {
  private String id;
  private String name;
  private GoalDuration duration;
  private BeforeAfter beforeAfter;
  private List<Plan> plans;

  public static Goal from(CreateGoalCommand command) {
    return Goal.builder()
        .id(IDGenerator.generateId())
        .name(command.name())
        .duration(command.duration())
        .beforeAfter(command.beforeAfter())
        .plans(command.plans().stream().map(planDto -> Plan.from(planDto.content())).toList())
        .build();
  }
}
