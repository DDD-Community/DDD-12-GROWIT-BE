package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTodayMissionUseCase {
  private final ToDoRepository toDoRepository;

  @Transactional
  public List<ToDo> execute(String userId, LocalDate today) {
    return toDoRepository.findByUserIdQuery(userId, today);
  }
}
