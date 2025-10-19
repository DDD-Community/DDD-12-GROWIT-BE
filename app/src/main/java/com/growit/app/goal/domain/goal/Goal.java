package com.growit.app.goal.domain.goal;

import static com.growit.app.common.util.message.ErrorCode.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.*;
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
  private String toBe;
  private GoalCategory category;
  private GoalUpdateStatus updateStatus;
  private List<Plan> plans;

  private Mentor mentor;

  @Getter(AccessLevel.NONE)
  private boolean isDelete;

  public static Goal from(CreateGoalCommand command) {
    return Goal.builder()
        .id(IDGenerator.generateId())
        .userId(command.userId())
        .name(command.name())
        .duration(command.duration())
        .toBe(command.toBe())
        .category(command.category())
        .updateStatus(GoalUpdateStatus.UPDATABLE)
        .plans(
            command.plans().stream()
                .map(planDto -> Plan.from(planDto, command.duration().startDate()))
                .toList())
        .mentor(Mentor.getMentorByCategory(command.category()))
        .isDelete(false)
        .build();
  }

  public void updateByCommand(UpdateGoalCommand command, GoalUpdateStatus status) {
    if (status == GoalUpdateStatus.ENDED) {
      throw new BadRequestException(GOAL_ENDED_DO_NOT_CHANGE.getCode());
    }

    this.name = command.name();

    if (status == GoalUpdateStatus.UPDATABLE) {
      this.duration = command.duration();
    }
  }

  public boolean checkProgress(GoalStatus status) {
    if (status == GoalStatus.NONE) {
      return true;
    } else if (status == GoalStatus.PROGRESS) {
      return updateStatus != GoalUpdateStatus.ENDED;
    } else {
      return updateStatus == GoalUpdateStatus.ENDED;
    }
  }

  public void updateByGoalUpdateStatus(GoalUpdateStatus updateStatus) {
    this.updateStatus = updateStatus;
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
        .filter(plan -> plan.getDuration().includes(date))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(GOAL_NOT_EXISTS_DATE.getCode()));
  }

  public Plan getPlanByPlanId(String planId) {
    return getPlans().stream()
        .filter(p -> p.getId().equals(planId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(GOAL_PLAN_NOT_FOUND.getCode()));
  }

  public boolean finished() {
    return updateStatus == GoalUpdateStatus.ENDED;
  }
}
