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
        userId, 
        request.getGoalId(), 
        request.getContent(), 
        request.getDate(),
        request.isImportant(),
        request.getRoutine());
  }

  public UpdateToDoCommand toUpdateCommand(String id, String userId, UpdateToDoRequest request) {
    return new UpdateToDoCommand(id, userId, request.getContent(), request.getDate());
  }

  public CompletedStatusChangeCommand toCompletedStatusChangeCommand(
      String id, String userId, CompletedStatusChangeRequest request) {
    return new CompletedStatusChangeCommand(id, userId, request.getCompleted(), request.getImportant());
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

  public GetDateRangeQueryFilter toGetDateRangeQueryFilter(String userId, String fromDate, String toDate) {
    LocalDate from;
    LocalDate to;
    try {
      from = LocalDate.parse(fromDate);
      to = LocalDate.parse(toDate);
    } catch (Exception e) {
      // Default to current date if parsing fails
      from = LocalDate.now();
      to = LocalDate.now();
    }
    return new GetDateRangeQueryFilter(userId, from, to);
  }
}
