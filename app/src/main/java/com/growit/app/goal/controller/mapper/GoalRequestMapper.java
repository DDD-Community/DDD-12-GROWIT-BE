package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class GoalRequestMapper {
  
  public CreateGoalCommand toCreateGoalCommand(String userId, CreateGoalRequest request) {
    // Validate that start date is first day of month and end date is last day of month
    validateGoalDuration(request.getStartDate(), request.getEndDate());
    
    // Generate weekly plans based on the duration
    List<PlanDto> plans = generateWeeklyPlans(request);
    
    return new CreateGoalCommand(
        userId,
        request.getName(),
        new GoalDuration(request.getStartDate(), request.getEndDate()),
        new BeforeAfter(request.getAsIs(), request.getToBe()),
        plans);
  }
  
  private void validateGoalDuration(java.time.LocalDate startDate, java.time.LocalDate endDate) {
    // Validate that start date is first day of month
    if (startDate.getDayOfMonth() != 1) {
      throw new IllegalArgumentException("목표 시작일은 월의 첫 날이어야 합니다.");
    }
    
    // Validate that end date is last day of month
    if (endDate.getDayOfMonth() != endDate.lengthOfMonth()) {
      throw new IllegalArgumentException("목표 종료일은 월의 마지막 날이어야 합니다.");
    }
    
    // Validate that end date is after start date
    if (!endDate.isAfter(startDate)) {
      throw new IllegalArgumentException("목표 종료일은 시작일보다 뒤여야 합니다.");
    }
  }
  
  private List<PlanDto> generateWeeklyPlans(CreateGoalRequest request) {
    long weeks = ChronoUnit.WEEKS.between(request.getStartDate(), request.getEndDate().plusDays(1));
    
    if (request.getPlans().size() != weeks) {
      throw new IllegalArgumentException(
          String.format("목표 기간(%d주)에 맞는 주간 계획이 필요합니다. 현재: %d개", 
              weeks, request.getPlans().size()));
    }
    
    return request.getPlans().stream()
        .map(planRequest -> new PlanDto(planRequest.getContent()))
        .toList();
  }
}