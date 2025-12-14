package com.growit.app.goal.domain.goal.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public interface GoalValidator {
  void checkGoalExists(String userId);

  void checkGoalDuration(GoalDuration duration);

  @Deprecated
  default void checkPlanExists(String userId, String goalId, String planId) {
  }

}
