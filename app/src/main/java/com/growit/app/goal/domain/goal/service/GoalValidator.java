package com.growit.app.goal.domain.goal.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public interface GoalValidator {
  void checkPlans(GoalDuration duration, List<PlanDto> plans) throws BadRequestException;

  void checkMyGoal(Goal goal, String userId) throws BadRequestException;

  void checkPlanExists(String userId, String goalId, String planId) throws NotFoundException;
}
