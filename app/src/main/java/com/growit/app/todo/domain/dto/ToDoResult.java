package com.growit.app.todo.domain.dto;

import com.growit.app.goal.domain.goal.plan.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToDoResult {
  private String id;
  private Plan plan;
}
