package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetToDoDateQueryFilter;
import com.growit.app.todo.domain.vo.FaceStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFaceStatusUseCase {
  private final ToDoRepository toDoRepository;

  public FaceStatus execute(String userId, String goalId) {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    GetToDoDateQueryFilter filter = new GetToDoDateQueryFilter(userId, goalId, yesterday);
    List<ToDo> toDos = toDoRepository.findByDateFilter(filter);
    List<ToDo> completedToDos = toDos.stream().filter(ToDo::isCompleted).toList();

    return FaceStatus.from(completedToDos.size(), toDos.size());
  }
}
