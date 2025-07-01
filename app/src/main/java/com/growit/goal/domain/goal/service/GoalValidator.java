package com.growit.goal.domain.goal.service;

import com.growit.common.exception.BadRequestException;
import com.growit.goal.domain.goal.Goal;
import com.growit.goal.domain.goal.dto.PlanDto;
import com.growit.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public interface GoalValidator {
  void checkPlans(GoalDuration duration, List<PlanDto> plans) throws BadRequestException;

  void checkMyGoal(Goal goal, String userId) throws BadRequestException;
}
