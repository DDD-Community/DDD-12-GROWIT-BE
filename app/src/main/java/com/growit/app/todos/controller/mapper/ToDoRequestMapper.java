package com.growit.app.todos.controller.mapper;

import com.growit.app.todos.controller.dto.CompletedStatusChangeRequest;
import com.growit.app.todos.controller.dto.CreateToDoRequest;
import com.growit.app.todos.controller.dto.UpdateToDoRequest;
import com.growit.app.todos.domain.dto.CompletedStatusChangeCommand;
import com.growit.app.todos.domain.dto.CreateToDoCommand;
import com.growit.app.todos.domain.dto.UpdateToDoCommand;
import org.springframework.stereotype.Component;

@Component
public class ToDoRequestMapper {

  public CreateToDoCommand toCreateCommand(String userId, CreateToDoRequest request) {
    return new CreateToDoCommand(
        userId, request.getGoalId(), request.getPlanId(), request.getContent(), request.getDate());
  }

  public UpdateToDoCommand toUpdateCommand(String id, String userId, UpdateToDoRequest request) {
    return new UpdateToDoCommand(id, userId, request.getContent(), request.getDate());
  }

  public CompletedStatusChangeCommand toCompletedStatusChangeCommand(
      String id, String userId, CompletedStatusChangeRequest request) {
    return new CompletedStatusChangeCommand(id, userId, request.isCompleted());
  }
}
