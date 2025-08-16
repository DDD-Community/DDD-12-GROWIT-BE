package com.growit.app.goal.domain.goal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.GoalCategory;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.growit.app.common.util.message.ErrorCode.*;

@Getter
@Builder
@AllArgsConstructor
public class Goal {
  private String id;
  @JsonIgnore
  private String userId;
  private String name;
  private GoalDuration duration;
  private String toBe;
  private GoalCategory category;
  @JsonIgnore
  private GoalUpdateStatus updateStatus;
  private List<Plan> plans;

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
      .isDelete(false)
      .build();
  }

  public void updateByCommand(UpdateGoalCommand command, GoalUpdateStatus status) {
    if (status == GoalUpdateStatus.ENDED) {
      throw new BadRequestException(GOAL_ENDED_DO_NOT_CHANGE.getCode());
    }

    if ((status == GoalUpdateStatus.PARTIALLY_UPDATABLE) && !Objects.equals(this.duration, command.duration())) {
      throw new BadRequestException(GOAL_PARTIALLY_DO_NOT_CHANGE_DURATION.getCode());
    }

    this.name = command.name();
    this.toBe = command.toBe();
    this.category = command.category();

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

  public boolean finished() {
    return updateStatus == GoalUpdateStatus.ENDED;
  }
}
