package com.growit.app.todos.domain.service;

import com.growit.app.common.exception.BadRequestException;
import java.time.LocalDate;

public interface ToDoValidator {
  void isDateInRange(LocalDate date) throws BadRequestException;

  void tooManyToDoCreated(LocalDate date, String userId) throws BadRequestException;

  void checkContent(String content) throws BadRequestException;
}
