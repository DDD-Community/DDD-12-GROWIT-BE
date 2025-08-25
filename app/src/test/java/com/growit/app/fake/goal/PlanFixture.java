package com.growit.app.fake.goal;

import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import java.time.LocalDate;

public class PlanFixture {

  public static Plan defaultPlan() {
    return new PlanBuilder().build();
  }

  public static Plan customPlan(
      String id, Integer weekOfMonth, String content, LocalDate start, LocalDate end) {
    PlanBuilder builder = new PlanBuilder();

    if (id != null) builder.id(id);
    if (weekOfMonth != null) builder.weekOfMonth(weekOfMonth);
    if (content != null) builder.content(content);
    if (start != null && end != null) builder.planDuration(new PlanDuration(start, end));

    return builder.build();
  }

  public static Plan planWithId(String id) {
    return new PlanBuilder().id(id).build();
  }

  public static Plan planWithWeekOfMonth(int weekOfMonth) {
    return new PlanBuilder().weekOfMonth(weekOfMonth).build();
  }

  public static Plan planWithContent(String content) {
    return new PlanBuilder().content(content).build();
  }

  public static Plan planWithDuration(LocalDate start, LocalDate end) {
    return new PlanBuilder().planDuration(new PlanDuration(start, end)).build();
  }
}

class PlanBuilder {
  private String id = "plan-1";
  private int weekOfMonth = 1;
  private String content = "주간 목표";
  private PlanDuration planDuration =
      new PlanDuration(LocalDate.of(2025, 6, 23), LocalDate.of(2025, 6, 29));

  public PlanBuilder id(String id) {
    this.id = id;
    return this;
  }

  public PlanBuilder weekOfMonth(int weekOfMonth) {
    this.weekOfMonth = weekOfMonth;
    return this;
  }

  public PlanBuilder content(String content) {
    this.content = content;
    return this;
  }

  public PlanBuilder planDuration(PlanDuration planDuration) {
    this.planDuration = planDuration;
    return this;
  }

  public Plan build() {
    return Plan.builder()
        .id(id)
        .weekOfMonth(weekOfMonth)
        .content(content)
        .duration(planDuration)
        .build();
  }
}
