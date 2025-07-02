package com.growit.app.fake.todos;

import com.growit.app.todos.controller.dto.CreateToDoRequest;
import com.growit.app.todos.domain.ToDo;
import java.time.LocalDate;

public class ToDoFixture {

  public static ToDo defaultToDo() {
    return new ToDoBuilder().build();
  }

  public static CreateToDoRequest defaultCreateToDoRequest() {
    return new CreateToDoRequest("goal-1", "plan-1", LocalDate.now(), "할 일 예시 내용입니다.");
  }

  public static ToDo customToDo(String id, String userId, LocalDate date, String planId) {
    return new ToDoBuilder()
        .id(id != null ? id : "todo-1")
        .userId(userId != null ? userId : "user-1")
        .date(date != null ? date : LocalDate.now())
        .planId(planId != null ? planId : "plan-1")
        .build();
  }
}

class ToDoBuilder {
  private String id = "todo-1";
  private String goalId = "goal-1";
  private String userId = "user-1";
  private String planId = "plan-1";
  private String content = "테스트 할 일입니다.";
  private LocalDate date = LocalDate.now();

  public ToDoBuilder id(String id) {
    this.id = id;
    return this;
  }

  public ToDoBuilder goalId(String goalId) {
    this.goalId = goalId;
    return this;
  }

  public ToDoBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public ToDoBuilder planId(String planId) {
    this.planId = planId;
    return this;
  }

  public ToDoBuilder content(String content) {
    this.content = content;
    return this;
  }

  public ToDoBuilder date(LocalDate date) {
    this.date = date;
    return this;
  }

  public ToDo build() {
    return ToDo.builder()
        .id(id)
        .goalId(goalId)
        .userId(userId)
        .planId(planId)
        .content(content)
        .date(date)
        .build();
  }
}
