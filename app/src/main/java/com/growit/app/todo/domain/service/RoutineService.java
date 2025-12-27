package com.growit.app.todo.domain.service;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.DeleteToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;

public interface RoutineService {
  ToDoResult createRoutineToDos(CreateToDoCommand command);

  ToDoResult updateRoutineToDos(ToDo existingToDo, UpdateToDoCommand command);

  void deleteRoutineToDos(ToDo existingToDo, DeleteToDoCommand command);
}
