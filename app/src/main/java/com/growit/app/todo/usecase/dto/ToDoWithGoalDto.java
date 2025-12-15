package com.growit.app.todo.usecase.dto;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToDoWithGoalDto {
  private final ToDo todo;
  private final Goal goal; // Can be null for "기타" todos
}