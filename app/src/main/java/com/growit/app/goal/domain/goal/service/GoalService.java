package com.growit.app.goal.domain.goal.service;

import static com.growit.app.common.util.message.ErrorCode.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService implements GoalValidator, GoalQuery {
  private final GoalRepository goalRepository;

  // 시작 일은 1주차 아무 요일 부터 설정 가능으로 변경 (종료일은 일요일, 과거는 생성 불가)
  @Override
  public void checkGoalDuration(GoalDuration duration) {
    LocalDate startDate = duration.startDate();
    LocalDate endDate = duration.endDate();
    LocalDate today = LocalDate.now().with(DayOfWeek.MONDAY);

    if (endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
      throw new BadRequestException(GOAL_DURATION_SUNDAY.getCode());
    }

    if (startDate.isBefore(today)) {
      throw new BadRequestException(GOAL_DURATION_START_AFTER_TODAY.getCode());
    }

    if (!endDate.isAfter(startDate)) {
      throw new BadRequestException(GOAL_DURATION_START_END.getCode());
    }
  }

  @Override
  public void checkPlans(GoalDuration duration, List<PlanDto> plans) throws BadRequestException {
    long weeks = duration.getWeekCount();
    if (weeks != plans.size()) {
      throw new BadRequestException(GOAL_PLAN_COUNT_NOT_MATCHED.getCode());
    }

    Set<Integer> days = new HashSet<>();
    for (PlanDto plan : plans) {
      if (!days.add(plan.weekOfMonth())) {
        throw new BadRequestException(GOAL_PLAN_DUPLICATE.getCode());
      }
    }
  }

  @Override
  public void checkPlanExists(String userId, String goalId, String planId)
      throws NotFoundException {
    Goal goal = getMyGoal(goalId, userId);
    boolean planExists = goal.getPlans().stream().anyMatch(plan -> plan.getId().equals(planId));

    if (!planExists) {
      throw new NotFoundException(GOAL_PLAN_NOT_FOUND.getCode());
    }
  }

  @Override
  public Goal getMyGoal(String id, String userId) throws NotFoundException {
    return goalRepository
        .findByIdAndUserId(id, userId)
        .orElseThrow(() -> new NotFoundException(GOAL_NOT_FOUND.getCode()));
  }
}
