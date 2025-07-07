package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.service.ToDoQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetWeeklyPlanUseCase {
  private final ToDoRepository toDoRepository;
  private final ToDoQuery toDoQuery;

  @Transactional
  public void execute() {}
}
