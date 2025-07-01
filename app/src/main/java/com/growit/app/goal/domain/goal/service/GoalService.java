package com.growit.app.goal.domain.goal.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class GoalService implements GoalValidator {
  @Override
  public void checkPlans(GoalDuration duration, List<PlanDto> plans) throws BadRequestException {
    long weeks = duration.getWeekCount();
    if (weeks != plans.size()) {
      throw new BadRequestException("설정한 날짜 범위와 주간 계획수가 일치하지 않습니다.");
    }

    Set<Integer> days = new HashSet<>();
    for (PlanDto plan : plans) {
      if (!days.add(plan.weekOfMonth())) {
        throw new BadRequestException("중복 되는 주차가 존재 합니다.");
      }
    }
  }

  public void checkMyGoal(Goal goal, String userId) throws BadRequestException {
    if (!goal.getUserId().equals(userId)) {
      throw new BadRequestException("삭제 권한이 없습니다.");
    }
  }
}
