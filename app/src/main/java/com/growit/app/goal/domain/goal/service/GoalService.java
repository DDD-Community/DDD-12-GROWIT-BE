package com.growit.app.goal.domain.goal.service;

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
public class GoalService implements GoalValidator {
  private final GoalRepository goalRepository;

  @Override
  public void checkGoalDuration(GoalDuration duration) {
    if (duration.startDate().getDayOfWeek() != DayOfWeek.MONDAY) {
      throw new BadRequestException("목표 시작일은 월요일 이여야 합니다.");
    }
    if (duration.endDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
      throw new BadRequestException("목표 종료일은 일요일 이여야 합니다. ");
    }
    if (!duration.endDate().isAfter(duration.startDate())) {
      throw new BadRequestException("목표 종료일은 시작일보다 뒤여야 합니다.");
    }
    if (duration.startDate().isBefore(LocalDate.now())) {
      throw new BadRequestException("목표 시작일은 오늘 또는 미래여야 합니다.");
    }
  }

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

  @Override
  public void checkPlanExists(String userId, String goalId, String planId)
      throws NotFoundException {
    Goal goal =
        goalRepository.findById(goalId).orElseThrow(() -> new NotFoundException("목표를 찾을 수 없습니다."));

    checkMyGoal(goal, userId);

    boolean planExists = goal.getPlans().stream().anyMatch(plan -> plan.getId().equals(planId));

    if (!planExists) {
      throw new NotFoundException("해당 목표에서 계획을 찾을 수 없습니다.");
    }
  }

  @Override
  public void checkMyGoal(Goal goal, String userId) throws BadRequestException {
    if (!goal.getUserId().equals(userId)) {
      throw new BadRequestException("해당 정보가 올바르지 않습니다.");
    }
  }
}
