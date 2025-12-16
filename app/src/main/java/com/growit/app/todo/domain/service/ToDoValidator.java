package com.growit.app.todo.domain.service;

import com.growit.app.common.exception.BadRequestException;
import java.time.LocalDate;

public interface ToDoValidator {
  void tooManyToDoCreated(LocalDate date, String userId, String goalId) throws BadRequestException;

  void tooManyToDoUpdated(LocalDate date, String userId, String goalId, String toDoId)
      throws BadRequestException;
}
