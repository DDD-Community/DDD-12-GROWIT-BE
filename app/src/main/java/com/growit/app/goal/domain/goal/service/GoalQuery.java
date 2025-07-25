package com.growit.app.goal.domain.goal.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;

public interface GoalQuery {
  Goal getMyGoal(String id, String userId) throws NotFoundException;
}
