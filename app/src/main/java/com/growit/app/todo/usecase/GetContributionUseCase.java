package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.service.ToDoQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetContributionUseCase {
  private final ToDoRepository toDoRepository;
  private final GoalRepository goalRepository;
  private final ToDoQuery toDoQuery;

  @Transactional(readOnly = true)
  public void execute(String userId, String goalId) {
    //    goalRepository.
  }
}
