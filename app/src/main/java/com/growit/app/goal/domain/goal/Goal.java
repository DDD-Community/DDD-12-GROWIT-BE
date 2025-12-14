package com.growit.app.goal.domain.goal;

import static com.growit.app.common.util.message.ErrorCode.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.vo.Planet;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import java.util.Objects;

public class Goal {
  private final String id;
  private String name;
  private final Planet planet;
  private GoalDuration duration;
  private GoalStatus status;
  private GoalUpdateStatus updateStatus;

  public Goal(String id, String name, Planet planet, GoalDuration duration) {
    this.id = Objects.requireNonNull(id, "Goal id cannot be null");
    this.name = Objects.requireNonNull(name, "Goal name cannot be null");
    this.planet = Objects.requireNonNull(planet, "Planet cannot be null");
    this.duration = Objects.requireNonNull(duration, "Duration cannot be null");
    this.status = GoalStatus.IN_PROGRESS;
    this.updateStatus = GoalUpdateStatus.UPDATABLE;
  }

  public static Goal create(String id, String name, Planet planet, GoalDuration duration) {
    return new Goal(id, name, planet, duration);
  }

  public static Goal from(CreateGoalCommand command) {
    // Create default planet based on category for now
    var planet = Planet.of("Earth", "/images/earth_done.png", "/images/earth_progress.png");

    return create(IDGenerator.generateId(), command.name(), planet, command.duration());
  }

  public void updateGoal(UpdateGoalCommand command) {
    validateCanBeUpdated();
    this.name = command.name();
    if (status.canBeUpdated()) {
      this.duration = command.duration();
    }
  }

  public void complete() {
    if (status.isCompleted()) {
      throw new BadRequestException(GOAL_ENDED_DO_NOT_CHANGE.getCode());
    }
    this.status = GoalStatus.COMPLETED;
  }

  public void updateName(String newName) {
    validateCanBeUpdated();
    this.name = Objects.requireNonNull(newName, "Goal name cannot be null");
  }

  public void updateStatus(GoalStatus newStatus) {
    if (newStatus == null) {
      throw new IllegalArgumentException("Goal status cannot be null");
    }

    // Business rule: Can only transition from IN_PROGRESS to COMPLETED
    if (this.status == GoalStatus.COMPLETED && newStatus == GoalStatus.IN_PROGRESS) {
      throw new BadRequestException("Cannot reopen a completed goal");
    }

    this.status = newStatus;
  }

  private void validateCanBeUpdated() {
    if (status.isCompleted()) {
      throw new BadRequestException(GOAL_ENDED_DO_NOT_CHANGE.getCode());
    }
  }

  // Getters

  public String getName() {
    return name;
  }

  public Planet getPlanet() {
    return planet;
  }

  public GoalDuration getDuration() {
    return duration;
  }

  public GoalStatus getStatus() {
    return status;
  }


  public boolean isCompleted() {
    return status.isCompleted();
  }

  public boolean isInProgress() {
    return status.isInProgress();
  }

  public String getId() {
    return id;
  }

  @Deprecated
  public String getUserId() {
    return "";
  }

  @Deprecated
  public String getToBe() {
    return "";
  }

  @Deprecated
  public Object getCategory() {
    return null;
  }

  @Deprecated
  public Object getMentor() {
    return null;
  }

  @Deprecated
  public boolean getDeleted() {
    return false;
  }

  @Deprecated
  public boolean finished() {
    return isCompleted();
  }

  @Deprecated
  public boolean checkProgress(GoalStatus status) {
    if (status == GoalStatus.NONE) {
      return true;
    } else if (status == GoalStatus.PROGRESS || status == GoalStatus.IN_PROGRESS) {
      return this.status.isInProgress();
    } else {
      return this.status.isCompleted();
    }
  }

  @Deprecated
  public void deleted() {
  }

  @Deprecated
  public void updateByGoalUpdateStatus(GoalUpdateStatus updateStatus) {
    updateStatus(updateStatus);
  }

  @Deprecated
  public void updateByCommand(UpdateGoalCommand command, GoalUpdateStatus status) {
    updateGoal(command);
  }

  @Deprecated
  public Object getPlans() {
    return java.util.Collections.emptyList();
  }



  public GoalUpdateStatus getUpdateStatus() {
    return updateStatus;
  }

  public void updateStatus(GoalUpdateStatus newUpdateStatus) {
    if (newUpdateStatus == null) {
      throw new IllegalArgumentException("Goal update status cannot be null");
    }
    this.updateStatus = newUpdateStatus;
    this.status = newUpdateStatus.toGoalStatus();
  }

}
