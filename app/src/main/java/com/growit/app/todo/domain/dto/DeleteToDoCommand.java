package com.growit.app.todo.domain.dto;

import com.growit.app.todo.domain.vo.RoutineDeleteType;

public record DeleteToDoCommand(String id, String userId, RoutineDeleteType routineDeleteType) {}
