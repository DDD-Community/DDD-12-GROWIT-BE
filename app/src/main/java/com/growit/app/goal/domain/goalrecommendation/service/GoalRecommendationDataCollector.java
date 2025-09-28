package com.growit.app.goal.domain.goalrecommendation.service;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.user.domain.user.User;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** 목표 추천 생성에 필요한 데이터를 수집하는 서비스 */
@Service
@RequiredArgsConstructor
public class GoalRecommendationDataCollector {

  private final ToDoRepository toDoRepository;
  private final GoalRetrospectRepository goalRetrospectRepository;

  /**
   * 목표 추천에 필요한 모든 데이터를 수집합니다.
   *
   * @param user 사용자
   * @param goal 현재 목표
   * @return 수집된 데이터
   */
  public GoalRecommendationData collectData(User user, Goal goal) {
    LocalDate today = LocalDate.now();
    LocalDate aMonthAgo = today.minusMonths(1);

    List<ToDo> monthlyTodos = getMonthlyTodos(user.getId(), aMonthAgo, today);
    List<String> pastRetrospects = getPastRetrospects(goal.getId(), aMonthAgo, today);

    return GoalRecommendationData.builder()
        .pastTodos(extractTodoContents(monthlyTodos))
        .completedTodos(extractCompletedTodoContents(monthlyTodos))
        .pastRetrospects(pastRetrospects)
        .pastWeeklyGoals(extractWeeklyGoalContents(goal))
        .remainingTime(calculateRemainingTime(goal))
        .build();
  }

  private List<ToDo> getMonthlyTodos(String userId, LocalDate startDate, LocalDate endDate) {
    return toDoRepository.findAllByUserIdAndCreatedAtBetween(
        userId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
  }

  private List<String> getPastRetrospects(String goalId, LocalDate startDate, LocalDate endDate) {
    return goalRetrospectRepository
        .findAllByGoalIdAndCreatedAtBetween(
            goalId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59))
        .stream()
        .map(GoalRetrospect::getContent)
        .collect(Collectors.toList());
  }

  private List<String> extractTodoContents(List<ToDo> todos) {
    return todos.stream().map(ToDo::getContent).collect(Collectors.toList());
  }

  private List<String> extractCompletedTodoContents(List<ToDo> todos) {
    return todos.stream()
        .filter(ToDo::isCompleted)
        .map(ToDo::getContent)
        .collect(Collectors.toList());
  }

  private List<String> extractWeeklyGoalContents(Goal goal) {
    return goal.getPlans().stream()
        .map(Plan::getContent)
        .filter(content -> content != null && !content.isBlank())
        .collect(Collectors.toList());
  }

  private String calculateRemainingTime(Goal goal) {
    LocalDate today = LocalDate.now();
    LocalDate dueDate = goal.getDuration().endDate();

    if (dueDate == null) {
      return "기간 정보 없음";
    }

    if (today.isAfter(dueDate)) {
      return "목표 기간 종료";
    }

    Period period = Period.between(today, dueDate);
    int months = period.getMonths();
    int days = period.getDays();

    if (months > 0 && days > 0) {
      return String.format("%d개월 %d일 남음", months, days);
    } else if (months > 0) {
      return String.format("%d개월 남음", months);
    } else if (days > 0) {
      return String.format("%d일 남음", days);
    } else {
      return "오늘 마감";
    }
  }
}
