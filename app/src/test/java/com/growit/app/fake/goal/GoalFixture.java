package com.growit.app.fake.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class GoalFixture {
  public static Goal defaultGoal() {
    return new GoalBuilder().build();
  }
}

class GoalBuilder {
  LocalDate today = LocalDate.now();
  LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  LocalDate thisSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
  String asIs = "ASIS";
  String toBe = "TOBE";

  private String id = "goal-1";
  private String userId = "user-1";
  private String name = "테스트 목표";
  private GoalDuration duration = new GoalDuration(thisMonday, thisSunday);
  private BeforeAfter beforeAfter = new BeforeAfter(asIs, toBe);
  private List<Plan> plans = new ArrayList<>();

  public GoalBuilder id(String id) {
    this.id = id;
    return this;
  }

  public GoalBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public GoalBuilder name(String name) {
    this.name = name;
    return this;
  }

  public GoalBuilder duration(GoalDuration duration) {
    this.duration = duration;
    return this;
  }

  public GoalBuilder beforeAfter(BeforeAfter beforeAfter) {
    this.beforeAfter = beforeAfter;
    return this;
  }

  public GoalBuilder plans(List<Plan> plans) {
    this.plans = plans;
    return this;
  }

  public Goal build() {
    return Goal.builder()
      .id(id)
      .userId(userId)
      .name(name)
      .duration(duration)
      .beforeAfter(beforeAfter)
      .plans(plans)
      .build();
  }

}
