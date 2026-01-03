package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.DeleteToDoCommand;
import com.growit.app.todo.domain.service.RoutineService;
import com.growit.app.todo.domain.service.ToDoHandler;
import com.growit.app.todo.domain.service.ToDoQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteToDoUseCase {
  private final ToDoRepository toDoRepository;
  private final ToDoHandler toDoHandler;
  private final ToDoQuery toDoQuery;
  private final RoutineService routineService;

  @Transactional
  public void execute(DeleteToDoCommand command) {
    ToDo toDo = toDoQuery.getMyToDo(command.id(), command.userId());

    // 루틴 삭제 처리
    if (toDo.getRoutine() != null && command.routineDeleteType() != null) {
      routineService.deleteRoutineToDos(toDo, command);
    } else {
      // 일반 ToDo 삭제
      toDo.deleted();
      toDoRepository.saveToDo(toDo);
    }

    if (toDo.getGoalId() != null) {
      toDoHandler.handle(toDo.getGoalId());
    }
  }
}
