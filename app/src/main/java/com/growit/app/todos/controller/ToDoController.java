package com.growit.app.todos.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.todos.controller.dto.CreateToDoRequest;
import com.growit.app.todos.controller.dto.UpdateToDoRequest;
import com.growit.app.todos.controller.mapper.ToDoRequestMapper;
import com.growit.app.todos.domain.dto.CreateToDoCommand;
import com.growit.app.todos.domain.dto.UpdateToDoCommand;
import com.growit.app.todos.usecase.CreateToDoUseCase;
import com.growit.app.todos.usecase.UpdateToDoUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
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
  private final CreateToDoUseCase createToDoUseCase;
  private final UpdateToDoUseCase updateToDoUseCase;

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
}
