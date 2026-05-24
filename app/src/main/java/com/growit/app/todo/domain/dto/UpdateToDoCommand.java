package com.growit.app.todo.domain.dto;

import com.growit.app.todo.domain.TodoCategory;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateToDoCommand(
    String id,
    String userId,
    String goalId,
    String content,
    LocalDate date,
    LocalTime time,
    TodoCategory category,
    Routine routine,
    RoutineUpdateType routineUpdateType) {}
