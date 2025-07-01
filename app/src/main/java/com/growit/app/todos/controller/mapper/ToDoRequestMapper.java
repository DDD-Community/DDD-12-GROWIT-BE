package com.growit.app.todos.controller.mapper;

import com.growit.app.todos.controller.dto.CreateToDoRequest;
import com.growit.app.todos.domain.CreateToDoCommand;
import org.springframework.stereotype.Component;

@Component
public class ToDoRequestMapper {

  public CreateToDoCommand toCommand(String userId, CreateToDoRequest request) {
    return new CreateToDoCommand(
        request.getGoalId(), request.getPlanId(), request.getContent(), request.getDate());
  }
}
