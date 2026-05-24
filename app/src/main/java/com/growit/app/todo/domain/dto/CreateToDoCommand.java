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
    Routine routine) {}
