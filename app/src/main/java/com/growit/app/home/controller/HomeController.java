package com.growit.app.home.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.home.controller.dto.HomeBootstrapResponse;
import com.growit.app.todo.controller.dto.response.ToDoWithGoalResponse;
import com.growit.app.todo.controller.dto.response.TodoCountByDateResponse;
import com.growit.app.todo.controller.mapper.ToDoRequestMapper;
import com.growit.app.todo.controller.mapper.ToDoResponseMapper;
import com.growit.app.todo.usecase.GetTodoCountByGoalInDateRangeUseCase;
import com.growit.app.todo.usecase.GetTodosWithGoalByDateUseCase;
import com.growit.app.user.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

  private final ToDoRequestMapper toDoRequestMapper;
  private final ToDoResponseMapper toDoResponseMapper;
  private final GetTodosWithGoalByDateUseCase getTodosWithGoalByDateUseCase;
  private final GetTodoCountByGoalInDateRangeUseCase getTodoCountByGoalInDateRangeUseCase;

  @GetMapping("/bootstrap")
  public ResponseEntity<ApiResponse<HomeBootstrapResponse>> bootstrap(
      @AuthenticationPrincipal User user,
      @RequestParam String date,
      @RequestParam String from,
      @RequestParam String to) {
    List<ToDoWithGoalResponse> todos =
        toDoResponseMapper.toToDoWithGoalResponseList(
            getTodosWithGoalByDateUseCase.execute(
                toDoRequestMapper.toGetDateQueryFilter(user.getId(), date)));
    List<TodoCountByDateResponse> todoCounts =
        toDoResponseMapper.toTodoCountByDateResponseList(
            getTodoCountByGoalInDateRangeUseCase.execute(
                toDoRequestMapper.toGetDateRangeQueryFilter(user.getId(), from, to)));

    return ResponseEntity.ok(
        ApiResponse.success(new HomeBootstrapResponse(user.isOnboarding(), todos, todoCounts)));
  }
}
