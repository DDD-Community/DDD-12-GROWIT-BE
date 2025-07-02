package com.growit.app.todos.domain.service;

import java.time.LocalDate;

public interface ToDoValidator {
  void isDateInRange(LocalDate date);

  void tooManyToDoCreated(LocalDate date, String userId);

  void checkContent(String content);
}
