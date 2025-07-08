package com.growit.app.todo.domain.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoService implements ToDoValidator, ToDoQuery {
  private final ToDoRepository toDoRepository;
  private static final int MAX_TO_COUNT = 10;

  @Override
  public void isDateInRange(LocalDate date, LocalDate thisWeekStartDate)
      throws BadRequestException {
    LocalDate today = LocalDate.now();
    LocalDate thisWeekSunday = today.with(java.time.DayOfWeek.SUNDAY);

    if (date.isBefore(thisWeekStartDate) || date.isAfter(thisWeekSunday)) {
      throw new BadRequestException("ToDo는 지난 주차, 이번 주차만 생성/수정할 수 있습니다.");
    }
  }

  @Override
  public void tooManyToDoCreated(LocalDate date, String userId, String planId)
      throws BadRequestException {
    int count = toDoRepository.countByToDo(date, userId, planId);
    if (count >= MAX_TO_COUNT) {
      throw new BadRequestException("하루에 할 일은 최대 10개까지만 등록할 수 있습니다.");
    }
  }

  @Override
  public void tooManyToDoUpdated(LocalDate date, String userId, String planId, String toDoId)
      throws BadRequestException {
    int count = toDoRepository.countByToDoWithToDoId(date, userId, planId, toDoId);
    if (count >= MAX_TO_COUNT) {
      throw new BadRequestException("해당 날짜에 이미 TODO 가 10개 입니다.");
    }
  }

  @Override
  public ToDo getMyToDo(String id, String userId) {
    ToDo todo =
        toDoRepository.findById(id).orElseThrow(() -> new NotFoundException("할 일 정보가 존재하지 않습니다."));

    if (!todo.getUserId().equals(userId)) {
      throw new NotFoundException("사용자 정보가 일치하지 않습니다.");
    }

    return todo;
  }
}
