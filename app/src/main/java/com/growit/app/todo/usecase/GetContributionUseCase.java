package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetContributionUseCase {
  private final GoalRepository goalRepository;
  private final ToDoRepository toDoRepository;

  @Transactional(readOnly = true)
  public List<String> execute(String userId, String goalId) {
    Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow();
    List<Plan> planList = goal.getPlans();
    List<String> planIds = planList.stream().map(Plan::getId).toList();
    System.out.println(planIds);
    final int days = 28;
    List<ToDo> toDoList = toDoRepository.findByPlanIdIn(planIds);
    Map<LocalDate, List<ToDo>> toDoByDate =
        toDoList.stream().collect(Collectors.groupingBy(ToDo::getDate));
    List<String> statusList = new ArrayList<>();
    for (int i = 0; i < days; i++) {
      LocalDate date = goal.getDuration().startDate().plusDays(i);
      List<ToDo> todos = toDoByDate.getOrDefault(date, Collections.emptyList());

      ToDoStatus status;
      if (todos.isEmpty()) {
        status = ToDoStatus.NONE;
      } else if (todos.stream().allMatch(todo -> !todo.isCompleted())) {
        status = ToDoStatus.NOT_STARTED;
      } else if (todos.stream().allMatch(ToDo::isCompleted)) {
        status = ToDoStatus.COMPLETED;
      } else {
        status = ToDoStatus.IN_PROGRESS;
      }
      statusList.add(status.name());
    }

    return statusList;
  }
}
