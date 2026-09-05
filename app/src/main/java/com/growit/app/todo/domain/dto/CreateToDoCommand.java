package com.growit.app.todo.domain.dto;

import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.ToDoCategory;
import java.time.LocalDate;

public record CreateToDoCommand(
    String userId,
    String goalId,
    String content,
    LocalDate date,
    boolean isImportant,
    ToDoCategory category,
    Routine routine) {
  /** Backward-compatible constructor for callers using the legacy importance flag. */
  public CreateToDoCommand(
      String userId,
      String goalId,
      String content,
      LocalDate date,
      boolean isImportant,
      Routine routine) {
    this(
        userId,
        goalId,
        content,
        date,
        isImportant,
        isImportant ? ToDoCategory.NOW : ToDoCategory.STEADY,
        routine);
  }
}
