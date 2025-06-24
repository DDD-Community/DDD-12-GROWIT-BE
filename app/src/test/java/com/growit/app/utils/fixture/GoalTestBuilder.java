package com.growit.app.utils.fixture;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalTestBuilder {
  LocalDate startDate = LocalDate.now();
  LocalDate endDate = LocalDate.now().plusDays(1);
  private String id = "goal-1";
  private String userId = "user-1";
  private String name = "테스트 목표";
  private GoalDuration duration = new GoalDuration(startDate, endDate);
  private BeforeAfter beforeAfter = null; // 필요하다면 기본값 생성
  private List<Plan> plans = new ArrayList<>();

  public GoalTestBuilder id(String id) {
    this.id = id;
    return this;
  }

  public GoalTestBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public GoalTestBuilder name(String name) {
    this.name = name;
    return this;
  }

  public GoalTestBuilder duration(GoalDuration duration) {
    this.duration = duration;
    return this;
  }

  public GoalTestBuilder beforeAfter(BeforeAfter beforeAfter) {
    this.beforeAfter = beforeAfter;
    return this;
  }

  public GoalTestBuilder plans(List<Plan> plans) {
    this.plans = plans;
    return this;
  }

  public Goal build() {
    return new Goal(id, userId, name, duration, beforeAfter, plans);
  }
}
