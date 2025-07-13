package com.growit.app.todo.controller.mapper;

import com.growit.app.todo.controller.dto.request.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.controller.dto.request.UpdateToDoRequest;
import com.growit.app.todo.domain.dto.*;
import java.time.LocalDate;
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

  public DeleteToDoCommand toDeleteCommand(String id, String userId) {
    return new DeleteToDoCommand(id, userId);
  }

  public GetToDoQueryFilter toGetQuery(String id, String userId) {
    return new GetToDoQueryFilter(id, userId);
  }

  public GetDateQueryFilter toGetDateQueryFilter(String userId, String date) {
    LocalDate today;
    try {
      today = LocalDate.parse(date);
    } catch (Exception e) {
      today = LocalDate.now();
    }
    return new GetDateQueryFilter(userId, today);
  }
}
