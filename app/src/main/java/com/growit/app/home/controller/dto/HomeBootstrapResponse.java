package com.growit.app.home.controller.dto;

import com.growit.app.todo.controller.dto.response.ToDoWithGoalResponse;
import com.growit.app.todo.controller.dto.response.TodoCountByDateResponse;
import java.util.List;

public record HomeBootstrapResponse(
    boolean hasOnboarded,
    List<ToDoWithGoalResponse> todos,
    List<TodoCountByDateResponse> todoCounts) {}
