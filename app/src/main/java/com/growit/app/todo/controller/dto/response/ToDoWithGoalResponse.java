package com.growit.app.todo.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ToDoWithGoalResponse {
  private TodoDto todo;
  private GoalDto goal;
}
