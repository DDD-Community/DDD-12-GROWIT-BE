package com.growit.goal.domain.goal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.common.util.IDGenerator;
import com.growit.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.goal.domain.goal.plan.Plan;
import com.growit.goal.domain.goal.vo.BeforeAfter;
import com.growit.goal.domain.goal.vo.GoalDuration;
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
        .plans(command.plans().stream().map(Plan::from).toList())
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
}
