package com.growit.app.todo.controller.dto.response;

import com.growit.app.todo.domain.ToDo;
import lombok.Getter;

@Getter
public class WeeklyPlanResponse {
  private String id;
  private String goalId;
  private String planId;
  private String date;
  private String content;
  private boolean isCompleted;

  public static WeeklyPlanResponse from(ToDo todo) {
    WeeklyPlanResponse res = new WeeklyPlanResponse();
    res.id = todo.getId();
    res.goalId = todo.getGoalId();
    res.planId = todo.getPlanId();
    res.date = todo.getDate().toString();
    res.content = todo.getContent();
    res.isCompleted = todo.isCompleted();
    return res;
  }
}
