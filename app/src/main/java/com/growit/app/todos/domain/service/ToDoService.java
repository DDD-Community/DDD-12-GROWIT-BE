package com.growit.app.todos.domain.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.todos.domain.ToDoRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoService implements ToDoValidator {
  private final ToDoRepository toDoRepository;

  @Override
  public void isDateInRange(LocalDate date) throws BadRequestException {
    LocalDate today = LocalDate.now();
    LocalDate lastWeekMonday = today.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
    LocalDate thisWeekSunday = today.with(java.time.DayOfWeek.SUNDAY);

    if (date.isBefore(lastWeekMonday) || date.isAfter(thisWeekSunday)) {
      throw new BadRequestException("ToDo는 지난 주, 이번 주만 생성할 수 있습니다.");
    }
  }

  public void tooManyToDoCreated(LocalDate date, String userId, String planId)
      throws BadRequestException {
    int count = toDoRepository.countByToDo(date, userId, planId);
    if (count >= 10) {
      throw new BadRequestException("하루에 할 일은 최대 10개까지만 등록할 수 있습니다.");
    }
  }

  public void checkContent(String content) throws BadRequestException {
    if (content == null || content.length() < 5 || content.length() >= 30) {
      throw new BadRequestException("내용은 5자 이상 30자 미만이어야 합니다.");
    }
  }
}
