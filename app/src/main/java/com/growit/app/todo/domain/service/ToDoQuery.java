package com.growit.app.todo.domain.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.todo.domain.ToDo;
import java.util.List;

public interface ToDoQuery {
  ToDo getMyToDo(String id, String userId) throws NotFoundException;

  List<ToDo> getToDosByGoalId(String goalId);
}
