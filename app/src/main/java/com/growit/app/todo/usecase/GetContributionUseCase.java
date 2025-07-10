package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.util.ToDoUtils;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetContributionUseCase {
  private final GoalRepository goalRepository;
  private final ToDoRepository toDoRepository;

  @Transactional(readOnly = true)
  public List<ToDoStatus> execute(String userId, String goalId) {
    Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow();
    List<String> planIds = goal.getPlans().stream().map(Plan::getId).toList();
    List<ToDo> toDoList = toDoRepository.findByPlanIdIn(planIds);

    return ToDoUtils.getContribution(goal, toDoList);
  }
}
