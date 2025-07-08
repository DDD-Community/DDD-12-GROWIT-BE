package com.growit.app.todo.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.todo.controller.dto.request.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.controller.dto.request.UpdateToDoRequest;
import com.growit.app.todo.controller.dto.response.WeeklyPlanResponse;
import com.growit.app.todo.controller.mapper.ToDoRequestMapper;
import com.growit.app.todo.controller.mapper.ToDoResponseMapper;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.CompletedStatusChangeCommand;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.usecase.*;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
  private final GetWeeklyPlanUseCase getWeeklyPlanUseCase;
  private final GetTodayMissionUseCase getTodayMissionUseCase;
  private final GetContributionUseCase getContributionUseCase;

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createToDo(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateToDoRequest request) {
    CreateToDoCommand command = toDoRequestMapper.toCreateCommand(user.getId(), request);
    String toDoId = createToDoUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(new IdDto(toDoId)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> updateToDo(
      @PathVariable String id,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody UpdateToDoRequest request) {
    UpdateToDoCommand command = toDoRequestMapper.toUpdateCommand(id, user.getId(), request);
    updateToDoUseCase.execute(command);
    return ResponseEntity.ok(ApiResponse.success("업데이트가 완료되었습니다."));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> statusChangeToDo(
      @PathVariable String id,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CompletedStatusChangeRequest request) {
    CompletedStatusChangeCommand command =
        toDoRequestMapper.toCompletedStatusChangeCommand(id, user.getId(), request);
    statusChangeToDoUseCase.execute(command);
    return ResponseEntity.ok(ApiResponse.success("상태 변경이 완료되었습니다."));
  }

  @GetMapping("/home/today-mission")
  public ResponseEntity<ApiResponse<List<ToDo>>> getTodayMission(
      @AuthenticationPrincipal User user) {
    LocalDate today = LocalDate.now();
    List<ToDo> toDoList = getTodayMissionUseCase.execute(user.getId(), today);
    return ResponseEntity.ok(new ApiResponse<>(toDoList));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ToDo>> getToDoById(
      @AuthenticationPrincipal User user, @PathVariable String id) {
    ToDo result = getToDoUseCase.execute(toDoRequestMapper.toGetQuery(id, user.getId()));
    return ResponseEntity.ok(ApiResponse.success(result));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteToDo(
      @AuthenticationPrincipal User user, @PathVariable String id) {
    deleteToDoUseCase.execute(toDoRequestMapper.toDeleteCommand(id, user.getId()));
    return ResponseEntity.ok(ApiResponse.success("삭제가 완료되었습니다."));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<Map<String, List<WeeklyPlanResponse>>>> getWeeklyTodos(
      @AuthenticationPrincipal User user,
      @RequestParam String goalId,
      @RequestParam String planId) {
    Map<DayOfWeek, List<ToDo>> grouped = getWeeklyPlanUseCase.execute(goalId, planId, user.getId());
    Map<String, List<WeeklyPlanResponse>> response =
        toDoResponseMapper.toWeeklyPlanResponse(grouped);
    return ResponseEntity.ok(new ApiResponse<>(response));
  }

  @GetMapping("/home/contribution")
  public ResponseEntity<ApiResponse<List<ToDo>>> getContribution(
      @AuthenticationPrincipal User user, @RequestParam String goalId) {
    getContributionUseCase.execute(user.getId(), goalId);
    return ResponseEntity.ok(new ApiResponse<>(null));
  }
}
