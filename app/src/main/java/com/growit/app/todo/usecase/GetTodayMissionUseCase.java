package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.service.ToDoQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTodayMissionUseCase {
  private final ToDoQuery todoQuery;
  private final ToDoRepository toDoRepository;

  @Transactional
  public void execute(String userId, String goalId, String planId) {}
}
