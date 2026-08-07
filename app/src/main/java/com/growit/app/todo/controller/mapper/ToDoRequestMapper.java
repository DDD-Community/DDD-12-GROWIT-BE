package com.growit.app.todo.controller.mapper;

import com.growit.app.todo.controller.dto.request.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.controller.dto.request.UpdateToDoRequest;
import com.growit.app.todo.controller.dto.response.RoutineDto;
import com.growit.app.todo.domain.dto.*;
import com.growit.app.todo.domain.vo.*;
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
        toDomainRoutine(request.getRoutine()));
  }

  public UpdateToDoCommand toUpdateCommand(String id, String userId, UpdateToDoRequest request) {
    return new UpdateToDoCommand(
        id,
        userId,
        request.getGoalId(),
        request.getContent(),
        request.getDate(),
        request.getImportant() != null ? request.getImportant() : false,
        toDomainRoutine(request.getRoutine()),
        // 클라이언트가 수정 범위를 지정하지 않았을 때 가장 파괴적인 ALL 로 해석하면
        // 반복 전체가 재생성되어 완료 이력이 사라진다. 비파괴적인 SINGLE 을 기본값으로 둔다.
        request.getRoutine() != null && request.getRoutineUpdateType() == null
            ? RoutineUpdateType.SINGLE
            : request.getRoutineUpdateType());
  }

  public CompletedStatusChangeCommand toCompletedStatusChangeCommand(
      String id, String userId, CompletedStatusChangeRequest request) {
    return new CompletedStatusChangeCommand(
        id, userId, request.getCompleted(), request.getImportant());
  }

  public DeleteToDoCommand toDeleteCommand(String id, String userId) {
    return new DeleteToDoCommand(id, userId, null);
  }

  public DeleteToDoCommand toDeleteCommand(
      String id, String userId, RoutineDeleteType routineDeleteType) {
    return new DeleteToDoCommand(id, userId, routineDeleteType);
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

  public GetDateRangeQueryFilter toGetDateRangeQueryFilter(
      String userId, String fromDate, String toDate) {
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

  private Routine toDomainRoutine(RoutineDto routineDto) {
    if (routineDto == null) {
      return null;
    }

    RoutineDuration duration = null;
    if (routineDto.getDuration() != null) {
      duration =
          RoutineDuration.of(
              routineDto.getDuration().getStartDate(), routineDto.getDuration().getEndDate());
    }

    RepeatType repeatType = RepeatType.valueOf(routineDto.getRepeatType());

    return Routine.of(duration, repeatType, routineDto.getRepeatDays());
  }
}
