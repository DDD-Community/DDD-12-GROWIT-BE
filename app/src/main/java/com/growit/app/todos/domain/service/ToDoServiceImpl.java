package com.growit.app.todos.domain.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ToDoServiceImpl implements ToDoService {
  @Override
  public void isDateInRange(LocalDate date) {
    LocalDate today = LocalDate.now();
    LocalDate lastWeekMonday = today.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
    LocalDate thisWeekSunday = today.with(java.time.DayOfWeek.SUNDAY);

    boolean inRange =
        (date.isEqual(lastWeekMonday) || date.isAfter(lastWeekMonday))
            && (date.isEqual(thisWeekSunday) || date.isBefore(thisWeekSunday));
    if (!inRange) {
      throw new IllegalArgumentException("ToDo는 지난 주, 이번 주만 생성할 수 있습니다.");
    }
  }
}
