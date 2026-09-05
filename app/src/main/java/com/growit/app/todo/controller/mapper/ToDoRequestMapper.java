package com.growit.app.todo.controller.mapper;

import com.growit.app.common.exception.BadRequestException;
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
        request.getCategory() != null
            ? request.getCategory()
            : (request.isImportant() ? ToDoCategory.NOW : ToDoCategory.STEADY),
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
        request.getCategory() != null
            ? request.getCategory()
            : (Boolean.TRUE.equals(request.getImportant()) ? ToDoCategory.NOW : ToDoCategory.STEADY),
        toDomainRoutine(request.getRoutine()),
        // 범위를 추측하지 않는다. 예전에는 routine 만 오면 ALL 로 해석해 반복 전체를 재생성했고,
        // 그 결과 완료 이력이 사라졌다. 지정되지 않은 범위는 UseCase 에서 거절한다.
        request.getRoutineUpdateType());
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

    return Routine.of(
        duration, toRepeatType(routineDto.getRepeatType()), routineDto.getRepeatDays());
  }

  /** repeatType 이 없거나 알 수 없는 값이면 valueOf 가 NPE/IAE 를 던져 500 이 된다. 400 으로 돌려준다. */
  private RepeatType toRepeatType(String repeatType) {
    if (repeatType == null || repeatType.isBlank()) {
      throw new BadRequestException("반복 주기(repeatType)는 필수입니다.");
    }

    try {
      return RepeatType.valueOf(repeatType);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("알 수 없는 반복 주기입니다: " + repeatType);
    }
  }
}
