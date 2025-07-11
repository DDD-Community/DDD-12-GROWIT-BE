package com.growit.app.todo.domain.service;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.util.List;

public interface ConventionCalculator {
  List<ToDoStatus> getContribution(Goal goal, List<ToDo> toDoList);
}
