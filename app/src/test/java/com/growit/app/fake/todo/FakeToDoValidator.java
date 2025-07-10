package com.growit.app.fake.todo;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.todo.domain.service.ToDoValidator;
import java.time.LocalDate;

public class FakeToDoValidator implements ToDoValidator {
  private boolean throwOnDateRange = false;
  private boolean throwOnTooManyCreate = false;
  private boolean throwOnTooManyUpdate = false;

  public void setThrowOnDateRange(boolean value) {
    this.throwOnDateRange = value;
  }

  public void setThrowOnTooManyCreate(boolean value) {
    this.throwOnTooManyCreate = value;
  }

  public void setThrowOnTooManyUpdate(boolean value) {
    this.throwOnTooManyUpdate = value;
  }

  @Override
  public void isDateInRange(LocalDate date, LocalDate thisWeekStartDate)
      throws BadRequestException {
    if (throwOnDateRange) {
      throw new BadRequestException("Date is out of range");
    }
  }

  @Override
  public void tooManyToDoCreated(LocalDate date, String userId, String planId)
      throws BadRequestException {
    if (throwOnTooManyCreate) {
      throw new BadRequestException("Too many todos created");
    }
  }

  @Override
  public void tooManyToDoUpdated(LocalDate date, String userId, String planId, String toDoId)
      throws BadRequestException {
    if (throwOnTooManyUpdate) {
      throw new BadRequestException("Too many todos updated");
    }
  }
}
