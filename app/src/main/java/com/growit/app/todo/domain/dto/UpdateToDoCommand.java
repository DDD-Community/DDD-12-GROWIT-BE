package com.growit.app.todo.domain.dto;

import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import java.time.LocalDate;

public record UpdateToDoCommand(
    String id,
    String userId,
    String goalId,
    String content,
    LocalDate date,
    boolean isImportant,
    Routine routine,
    RoutineUpdateType routineUpdateType) {}
