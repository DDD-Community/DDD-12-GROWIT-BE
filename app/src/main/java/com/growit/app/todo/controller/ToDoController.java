package com.growit.app.todo.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.todo.controller.dto.request.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.controller.dto.request.UpdateToDoRequest;
import com.growit.app.todo.controller.dto.response.ToDoResponse;
import com.growit.app.todo.controller.dto.response.ToDoWithGoalResponse;
import com.growit.app.todo.controller.dto.response.TodoCountByDateResponse;
import com.growit.app.todo.controller.dto.response.TodoDto;
import com.growit.app.todo.controller.mapper.ToDoRequestMapper;
import com.growit.app.todo.controller.mapper.ToDoResponseMapper;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.CompletedStatusChangeCommand;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.RoutineDeleteType;
import com.growit.app.todo.usecase.*;
import com.growit.app.todo.usecase.dto.ToDoWithGoalDto;
import com.growit.app.todo.usecase.dto.TodoCountByDateDto;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class ToDoController {

  private final ToDoRequestMapper toDoRequestMapper;
  private final ToDoResponseMapper toDoResponseMapper;
  private final CreateToDoUseCase createToDoUseCase;
  private final UpdateToDoUseCase updateToDoUseCase;
  private final CompletedStatusChangeToDoUseCase statusChangeToDoUseCase;
  private final GetToDoUseCase getToDoUseCase;
  private final DeleteToDoUseCase deleteToDoUseCase;
  private final GetTodosWithGoalByDateUseCase getTodosWithGoalByDateUseCase;
  private final GetTodoCountByGoalInDateRangeUseCase getTodoCountByGoalInDateRangeUseCase;

  @PostMapping
  public ResponseEntity<ApiResponse<ToDoResponse>> createToDo(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateToDoRequest request) {
    CreateToDoCommand command = toDoRequestMapper.toCreateCommand(user.getId(), request);
    ToDoResult result = createToDoUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(toDoResponseMapper.toToDoResponse(result)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateToDo(
      @PathVariable String id,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody UpdateToDoRequest request) {
    UpdateToDoCommand command = toDoRequestMapper.toUpdateCommand(id, user.getId(), request);
    updateToDoUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> changeStatus(
      @PathVariable String id,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CompletedStatusChangeRequest request) {
    CompletedStatusChangeCommand command =
        toDoRequestMapper.toCompletedStatusChangeCommand(id, user.getId(), request);
    statusChangeToDoUseCase.execute(command);
    return ResponseEntity.ok(ApiResponse.success("상태 변경이 완료되었습니다."));
  }

  @GetMapping(params = "date")
  public ResponseEntity<ApiResponse<List<ToDoWithGoalResponse>>> getTodosByDate(
      @AuthenticationPrincipal User user, @RequestParam String date) {
    List<ToDoWithGoalDto> todoList =
        getTodosWithGoalByDateUseCase.execute(
            toDoRequestMapper.toGetDateQueryFilter(user.getId(), date));
    return ResponseEntity.ok(
        ApiResponse.success(toDoResponseMapper.toToDoWithGoalResponseList(todoList)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<TodoDto>> getToDoById(
      @AuthenticationPrincipal User user, @PathVariable String id) {
    ToDo result = getToDoUseCase.execute(toDoRequestMapper.toGetQuery(id, user.getId()));
    TodoDto todoDto = toDoResponseMapper.toTodoDto(result);
    return ResponseEntity.ok(ApiResponse.success(todoDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteToDo(
      @AuthenticationPrincipal User user,
      @PathVariable String id,
      @RequestParam(required = false) RoutineDeleteType routineDeleteType) {
    deleteToDoUseCase.execute(
        toDoRequestMapper.toDeleteCommand(id, user.getId(), routineDeleteType));
    return ResponseEntity.ok(ApiResponse.success("삭제가 완료되었습니다."));
  }

  @GetMapping(
      value = "/count",
      params = {"from", "to"})
  public ResponseEntity<ApiResponse<List<TodoCountByDateResponse>>> getTodoCountByDateRange(
      @AuthenticationPrincipal User user, @RequestParam String from, @RequestParam String to) {
    List<TodoCountByDateDto> todoCountList =
        getTodoCountByGoalInDateRangeUseCase.execute(
            toDoRequestMapper.toGetDateRangeQueryFilter(user.getId(), from, to));
    List<TodoCountByDateResponse> response =
        toDoResponseMapper.toTodoCountByDateResponseList(todoCountList);
    return ResponseEntity.ok(new ApiResponse<>(response));
  }
}
