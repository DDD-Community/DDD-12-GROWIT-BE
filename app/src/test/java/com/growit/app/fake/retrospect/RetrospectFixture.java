package com.growit.app.fake.retrospect;

import com.growit.app.fake.goal.PlanFixture;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.GetRetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.dto.UpdateRetrospectCommand;
import com.growit.app.retrospect.usecase.dto.RetrospectWithPlan;

public class RetrospectFixture {
  public static Retrospect defaultRetrospect() {
    return new RetrospectBuilder().build();
  }

  public static RetrospectWithPlan defaultRetrospectWithPlan() {
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    String planId = retrospect.getPlanId();
    Plan plan = PlanFixture.customPlan(planId, 1, null, null, null);
    return new RetrospectWithPlan(retrospect, plan);
  }

  public static Retrospect customRetrospect(
      String id, String userId, String goalId, String planId, String content) {
    return new RetrospectBuilder()
        .id(id)
        .userId(userId)
        .goalId(goalId)
        .planId(planId)
        .content(content)
        .build();
  }

  public static RetrospectQueryFilter defaultQueryFilter() {
    String userId = "user-123";
    String goalId = "goal-123";
    String planId = "plan-123";
    return new RetrospectQueryFilter(goalId, planId, userId);
  }

  public static GetRetrospectQueryFilter defaultGetQueryFilter() {
    String id = "retrospect-123";
    String userId = "user-123";
    return new GetRetrospectQueryFilter(id, userId);
  }

  public static CreateRetrospectRequest defaultCreateRetrospectRequest() {
    return new CreateRetrospectRequest(
        "goal-123", "plan-456", "이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.");
  }

  public static CreateRetrospectCommand defaultCreateRetrospectCommand() {
    return new CreateRetrospectCommand(
        "goal-123",
        "plan-456",
        "user-789",
        "이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.");
  }

  public static CreateRetrospectCommand createRetrospectCommandWithContent(
      String goalId, String userId, String planId, String content) {
    return new CreateRetrospectCommand(goalId, planId, userId, content);
  }

  public static UpdateRetrospectRequest defaultUpdateRetrospectRequest() {
    return new UpdateRetrospectRequest(
        "이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.");
  }

  public static UpdateRetrospectCommand defaultUpdateRetrospectCommand() {
    return new UpdateRetrospectCommand(
        "id", "userId", "이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.");
  }

  public static UpdateRetrospectCommand updateRetrospectCommand(
      String id, String userId, String content) {
    return new UpdateRetrospectCommand(id, userId, content);
  }
}

class RetrospectBuilder {
  private String id = "retrospect-123";
  private String userId = "user-123";
  private String goalId = "goal-123";
  private String planId = "plan-1";
  private String content = "이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.";

  public RetrospectBuilder id(String id) {
    this.id = id;
    return this;
  }

  public RetrospectBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public RetrospectBuilder goalId(String goalId) {
    this.goalId = goalId;
    return this;
  }

  public RetrospectBuilder planId(String planId) {
    this.planId = planId;
    return this;
  }

  public RetrospectBuilder content(String content) {
    this.content = content;
    return this;
  }

  public Retrospect build() {
    return Retrospect.builder()
        .id(id)
        .userId(userId)
        .goalId(goalId)
        .planId(planId)
        .content(content)
        .build();
  }
}
