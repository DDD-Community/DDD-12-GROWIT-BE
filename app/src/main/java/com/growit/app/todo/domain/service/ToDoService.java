package com.growit.app.todo.domain.service;

import static com.growit.app.todo.domain.vo.ToDoStatus.getStatus;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoService implements ToDoValidator, ToDoQuery, ConventionCalculator {
  private final ToDoRepository toDoRepository;
  private static final int MAX_TO_COUNT = 10;

  private void validateMaxToDoCount(
      LocalDate date, String userId, String planId, Optional<String> toDoId, String errorMessage) {
    GetCountByDateQueryFilter filter = new GetCountByDateQueryFilter(date, userId, planId, toDoId);
    int count = toDoRepository.countByDateQuery(filter);
    if (count >= MAX_TO_COUNT) {
      throw new BadRequestException(errorMessage);
    }
  }

  @Override
  public void tooManyToDoCreated(LocalDate date, String userId, String planId)
      throws BadRequestException {
    validateMaxToDoCount(date, userId, planId, Optional.empty(), "하루에 할 일은 최대 10개까지만 등록할 수 있습니다.");
  }

  @Override
  public void tooManyToDoUpdated(LocalDate date, String userId, String planId, String toDoId)
      throws BadRequestException {
    validateMaxToDoCount(
        date, userId, planId, Optional.ofNullable(toDoId), "해당 날짜에 이미 TODO 가 10개 입니다.");
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
  public List<ToDoStatus> getContribution(Goal goal, List<ToDo> toDoList) {
    LocalDate start = goal.getDuration().startDate();
    LocalDate today = LocalDate.now();

    Map<LocalDate, List<ToDo>> toDoByDate =
        toDoList.stream().collect(Collectors.groupingBy(ToDo::getDate));

    List<ToDoStatus> statusList = new ArrayList<>();

    for (LocalDate date = start; !date.isAfter(today); date = date.plusDays(1)) {
      statusList.add(getStatusForDate(date, toDoByDate));
    }

    return statusList;
  }

  private static ToDoStatus getStatusForDate(
      LocalDate date, Map<LocalDate, List<ToDo>> toDoByDate) {
    List<ToDo> todos = toDoByDate.getOrDefault(date, Collections.emptyList());
    return getStatus(todos);
  }
}
