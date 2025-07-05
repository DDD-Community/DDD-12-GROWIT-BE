package com.growit.app.todo.controller.mapper;

import com.growit.app.todo.controller.dto.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.CreateToDoRequest;
import com.growit.app.todo.controller.dto.UpdateToDoRequest;
import com.growit.app.todo.domain.dto.CompletedStatusChangeCommand;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.GetTodDoQueryFilter;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
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

  public GetTodDoQueryFilter toGetQuery(String id, String userId) {
    return new GetTodDoQueryFilter(id, userId);
  }
}
