package com.growit.app.goal.domain.goal;

import static com.growit.app.common.util.message.ErrorCode.GOAL_NOT_EXISTS_DATE;
import static com.growit.app.common.util.message.ErrorCode.GOAL_PLAN_NOT_FOUND;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Goal {
  private String id;
  @JsonIgnore private String userId;
  private String name;
  private GoalDuration duration;
  private BeforeAfter beforeAfter;
  private List<Plan> plans;

  @Getter(AccessLevel.NONE)
  private boolean isDelete;

  public static Goal from(CreateGoalCommand command) {
    return Goal.builder()
        .id(IDGenerator.generateId())
        .userId(command.userId())
        .name(command.name())
        .duration(command.duration())
        .beforeAfter(command.beforeAfter())
        .plans(
            command.plans().stream()
                .map(planDto -> Plan.from(planDto, command.duration().startDate()))
                .toList())
        .isDelete(false)
        .build();
  }

  public void deleted() {
    this.isDelete = true;
  }

  @JsonIgnore
  public boolean getDeleted() {
    return isDelete;
  }

  public Plan getPlanByDate(LocalDate date) {
    return plans.stream()
        .filter(plan -> plan.getPlanDuration().includes(date))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(GOAL_NOT_EXISTS_DATE.getCode()));
  }

  public Plan getPlanByPlanId(String planId) {
    return getPlans().stream()
        .filter(p -> p.getId().equals(planId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(GOAL_PLAN_NOT_FOUND.getCode()));
  }
}
