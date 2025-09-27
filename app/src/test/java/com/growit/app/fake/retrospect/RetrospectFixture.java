package com.growit.app.fake.retrospect;

import com.growit.app.fake.goal.PlanFixture;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.controller.retrospect.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.retrospect.dto.request.KPTDto;
import com.growit.app.retrospect.controller.retrospect.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.GetRetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.dto.UpdateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.vo.KPT;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;

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
      String id, String userId, String goalId, String planId, KPT kpt) {
    return new RetrospectBuilder()
        .id(id)
        .userId(userId)
        .goalId(goalId)
        .planId(planId)
        .kpt(kpt)
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
    KPTDto kptDto = new KPTDto("계획대로 진행했던 부분을 작성합니다.", "어려웠던 문제점들을 작성합니다.", "다음에 시도해볼 방법들을 작성합니다.");
    return new CreateRetrospectRequest("goal-123", "plan-456", "", kptDto);
  }

  public static CreateRetrospectCommand defaultCreateRetrospectCommand() {
    KPT kpt = new KPT("계획대로 진행했던 부분", "어려웠던 문제점들", "다음에 시도해볼 방법들");
    return new CreateRetrospectCommand("goal-123", "plan-456", "user-789", kpt);
  }

  public static CreateRetrospectCommand createRetrospectCommandWithKPT(
      String goalId, String userId, String planId, KPT kpt) {
    return new CreateRetrospectCommand(goalId, planId, userId, kpt);
  }

  public static UpdateRetrospectRequest defaultUpdateRetrospectRequest() {
    KPTDto kptDto = new KPTDto("수정된 Keep 내용", "수정된 Problem 내용", "수정된 Try 내용");
    return new UpdateRetrospectRequest(kptDto);
  }

  public static UpdateRetrospectCommand defaultUpdateRetrospectCommand() {
    KPT kpt = new KPT("수정된 Keep 내용", "수정된 Problem 내용", "수정된 Try 내용");
    return new UpdateRetrospectCommand("id", "userId", kpt);
  }

  public static UpdateRetrospectCommand updateRetrospectCommand(String id, String userId, KPT kpt) {
    return new UpdateRetrospectCommand(id, userId, kpt);
  }
}

class RetrospectBuilder {
  private String id = "retrospect-123";
  private String userId = "user-123";
  private String goalId = "goal-123";
  private String planId = "plan-1";
  private KPT kpt = new KPT("계획대로 진행했던 부분", "어려웠던 문제점들", "다음에 시도해볼 방법들");

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

  public RetrospectBuilder kpt(KPT kpt) {
    this.kpt = kpt;
    return this;
  }

  public Retrospect build() {
    return Retrospect.builder()
        .id(id)
        .userId(userId)
        .goalId(goalId)
        .planId(planId)
        .kpt(kpt)
        .build();
  }
}
