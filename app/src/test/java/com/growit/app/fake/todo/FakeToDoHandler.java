package com.growit.app.fake.todo;

import com.growit.app.todo.domain.service.ToDoHandler;

public class FakeToDoHandler implements ToDoHandler {
  public boolean called = false;
  public String lastUid;

  @Override
  public void handle(String goalId) {
    this.called = true;
    this.lastUid = goalId;
  }
}
