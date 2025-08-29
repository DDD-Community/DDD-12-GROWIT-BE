package com.growit.app.retrospect.domain.goalretrospect.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;

public interface GoalRetrospectQuery {
  GoalRetrospect getMyGoalRetrospect(String id, String userId) throws NotFoundException;

  boolean isExistsByGoalId(String goalId);
}
