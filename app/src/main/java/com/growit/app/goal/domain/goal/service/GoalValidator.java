package com.growit.app.goal.domain.goal.service;

import com.growit.app.goal.domain.goal.vo.GoalDuration;

public interface GoalValidator {
  void checkGoalDuration(GoalDuration duration);
}
