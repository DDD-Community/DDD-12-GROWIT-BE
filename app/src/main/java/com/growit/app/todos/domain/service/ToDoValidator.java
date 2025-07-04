package com.growit.app.todos.domain.service;

import com.growit.app.common.exception.BadRequestException;
import java.time.LocalDate;

public interface ToDoValidator {
  void isDateInRange(LocalDate date, String goalId) throws BadRequestException;

  void tooManyToDoCreated(LocalDate date, String userId, String planId) throws BadRequestException;

  void tooManyToDoUpdated(LocalDate date, String userId, String planId, String toDoId)
      throws BadRequestException;
}
