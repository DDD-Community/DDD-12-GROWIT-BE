package com.growit.app.todos.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.todos.controller.dto.CreateToDoRequest;
import com.growit.app.todos.controller.mapper.ToDoRequestMapper;
import com.growit.app.todos.domain.CreateToDoCommand;
import com.growit.app.todos.usecase.CreateToDoUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class ToDoController {

  private final ToDoRequestMapper toDoRequestMapper;
  private final CreateToDoUseCase createToDoUseCase;

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createToDo(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateToDoRequest request) {
    CreateToDoCommand command = toDoRequestMapper.toCommand(user.getId(), request);
    String toDoId = createToDoUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(new IdDto(toDoId)));
  }
}
