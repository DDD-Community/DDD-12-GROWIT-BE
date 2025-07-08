package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetContributionUseCase {
  private final ToDoRepository toDoRepository;

  @Transactional
  public void execute(String userId, String goalId) {}
}
