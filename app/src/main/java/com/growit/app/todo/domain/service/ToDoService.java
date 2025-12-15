package com.growit.app.todo.domain.service;

import static com.growit.app.common.util.message.ErrorCode.GOAL_NOT_FOUND;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import java.time.LocalDate;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoService implements ToDoValidator, ToDoQuery, ToDoHandler {
  private final ToDoRepository toDoRepository;
  private final GoalRepository goalRepository;
  private static final int MAX_TO_COUNT = 10;

  private void validateMaxToDoCount(
      LocalDate date, String userId, String goalId, Optional<String> toDoId, String errorMessage) {
    GetCountByDateQueryFilter filter = new GetCountByDateQueryFilter(date, userId, goalId, toDoId);
    int count = toDoRepository.countByDateQuery(filter);
    if (count >= MAX_TO_COUNT) {
      throw new BadRequestException(errorMessage);
    }
  }

  @Override
  public void tooManyToDoCreated(LocalDate date, String userId, String goalId)
      throws BadRequestException {
    validateMaxToDoCount(date, userId, goalId, Optional.empty(), "하루에 할 일은 최대 10개까지만 등록할 수 있습니다.");
  }

  @Override
  public void tooManyToDoUpdated(LocalDate date, String userId, String goalId, String toDoId)
      throws BadRequestException {
    validateMaxToDoCount(
        date, userId, goalId, Optional.ofNullable(toDoId), "해당 날짜에 이미 TODO 가 10개 입니다.");
  }

  @Override
  public ToDo getMyToDo(String id, String userId) {
    ToDo todo =
        toDoRepository.findById(id).orElseThrow(() -> new NotFoundException("할 일 정보가 존재하지 않습니다."));

    if (!todo.getUserId().equals(userId)) {
      throw new NotFoundException("사용자 정보가 일치하지 않습니다.");
    }

    if (todo.isDeleted()) {
      throw new NotFoundException("존재하지 않는 ToDo 입니다.");
    }

    return todo;
  }

  @Override
  public List<ToDo> getToDosByGoalId(String goalId) {
    return toDoRepository.findByGoalId(goalId);
  }

  @Override
  public void handle(String id) {
    Goal goal =
        goalRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(GOAL_NOT_FOUND.getCode()));

    // Note: The goal update status logic has been removed as it's no longer needed.
    // Goals now have their own status management system.
  }
}
