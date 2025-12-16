package com.growit.app.todo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToDoResult {
  private String id;
  // Plan field removed as plan domain has been deleted
}
