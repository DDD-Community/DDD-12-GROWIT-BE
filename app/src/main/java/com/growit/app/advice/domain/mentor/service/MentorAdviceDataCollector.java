package com.growit.app.advice.domain.mentor.service;

import com.growit.app.advice.domain.mentor.vo.MentorAdviceData;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.user.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** 멘토 조언 생성에 필요한 데이터를 수집하는 서비스 */
@Service
@RequiredArgsConstructor
public class MentorAdviceDataCollector {

  private final ToDoRepository toDoRepository;
  private final GoalRetrospectRepository goalRetrospectRepository;

  /**
   * 멘토 조언에 필요한 모든 데이터를 수집합니다.
   *
   * @param user 사용자
   * @param goal 현재 목표
   * @return 수집된 데이터
   */
  public MentorAdviceData collectData(User user, Goal goal) {
    LocalDate today = LocalDate.now();
    LocalDate aWeekAgo = today.minusWeeks(1);

    List<ToDo> weeklyTodos = getWeeklyTodos(user.getId(), aWeekAgo, today);
    List<String> weeklyRetrospects = getWeeklyRetrospects(goal.getId(), aWeekAgo, today);

    return MentorAdviceData.builder()
        .completedTodos(extractCompletedTodoContents(weeklyTodos))
        .incompleteTodos(extractIncompleteTodoContents(weeklyTodos))
        .weeklyRetrospects(weeklyRetrospects)
        .pastWeeklyGoals(extractWeeklyGoalContents(goal))
        .overallGoal(goal.getName())
        .build();
  }

  private List<ToDo> getWeeklyTodos(String userId, LocalDate startDate, LocalDate endDate) {
    return toDoRepository.findAllByUserIdAndCreatedAtBetween(
        userId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
  }

  private List<String> getWeeklyRetrospects(String goalId, LocalDate startDate, LocalDate endDate) {
    return goalRetrospectRepository
        .findAllByGoalIdAndCreatedAtBetween(
            goalId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59))
        .stream()
        .map(GoalRetrospect::getContent)
        .collect(Collectors.toList());
  }

  private List<String> extractCompletedTodoContents(List<ToDo> todos) {
    return todos.stream()
        .filter(ToDo::isCompleted)
        .map(ToDo::getContent)
        .collect(Collectors.toList());
  }

  private List<String> extractIncompleteTodoContents(List<ToDo> todos) {
    return todos.stream()
        .filter(todo -> !todo.isCompleted())
        .map(ToDo::getContent)
        .collect(Collectors.toList());
  }

  private List<String> extractWeeklyGoalContents(Goal goal) {
    if (goal.getPlans() == null || goal.getPlans().isEmpty()) {
      return List.of(); // 빈 리스트 반환
    }
    return goal.getPlans().stream().map(Plan::getContent).collect(Collectors.toList());
  }
}
