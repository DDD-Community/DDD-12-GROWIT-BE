package com.growit.app.todo.domain.dto;

import com.growit.app.todo.domain.TodoCategory;
import com.growit.app.todo.domain.vo.Routine;
import java.time.LocalDate;

public record CreateToDoCommand(
    String userId,
    String goalId,
    String content,
    LocalDate date,
    TodoCategory category,
    Routine routine) {}
