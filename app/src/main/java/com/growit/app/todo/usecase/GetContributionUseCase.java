package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.service.ConventionCalculator;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetContributionUseCase {
  private final ToDoRepository toDoRepository;
  private final ConventionCalculator conventionCalculator;
  private final GoalQuery goalQuery;

  @Transactional(readOnly = true)
  public List<ToDoStatus> execute(String userId, String goalId) {
    Goal goal = goalQuery.getMyGoal(goalId, userId);
    List<ToDo> toDoList = toDoRepository.findByGoalId(goalId);

    return conventionCalculator.getContribution(goal, toDoList);
  }
}
