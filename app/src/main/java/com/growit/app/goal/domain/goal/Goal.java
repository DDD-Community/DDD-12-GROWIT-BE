package com.growit.app.goal.domain.goal;

import static com.growit.app.common.util.message.ErrorCode.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.domain.goal.planet.Planet;
import java.util.Objects;

public class Goal {
  private final String id;
  private final String userId;
  private String name;
  private final Planet planet;
  private GoalDuration duration;
  private GoalStatus status;
  private boolean deleted = false;

  public Goal(String id, String userId, String name, Planet planet, GoalDuration duration) {
    this.id = Objects.requireNonNull(id, "Goal id cannot be null");
    this.userId = Objects.requireNonNull(userId, "User id cannot be null");
    this.name = Objects.requireNonNull(name, "Goal name cannot be null");
    this.planet = Objects.requireNonNull(planet, "Planet cannot be null");
    this.duration = Objects.requireNonNull(duration, "Duration cannot be null");
    this.status = GoalStatus.PROGRESS;
  }

  public static Goal create(String id, String userId, String name, Planet planet, GoalDuration duration) {
    return new Goal(id, userId, name, planet, duration);
  }

  public static Goal from(CreateGoalCommand command, Planet planet) {
    return create(IDGenerator.generateId(), command.userId(), command.name(), planet, command.duration());
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

    // Business rule: Can only transition from PROGRESS to COMPLETED
    if (this.status == GoalStatus.COMPLETED && newStatus == GoalStatus.PROGRESS) {
      throw new BadRequestException("Cannot reopen a completed goal");
    }

    this.status = newStatus;
  }

  public void delete() {
    this.deleted = true;
  }

  private void validateCanBeUpdated() {
    if (status.isCompleted()) {
      throw new BadRequestException(GOAL_ENDED_DO_NOT_CHANGE.getCode());
    }
    if (deleted) {
      throw new BadRequestException("Cannot update deleted goal");
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

  public String getUserId() {
    return userId;
  }

  public boolean isDeleted() {
    return deleted;
  }





}
