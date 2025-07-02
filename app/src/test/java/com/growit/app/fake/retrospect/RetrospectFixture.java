package com.growit.app.fake.retrospect;

import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;

public class RetrospectFixture {
  public static Retrospect defaultRetrospect() {
    return new RetrospectBuilder().build();
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
}

class RetrospectBuilder {
  private String id = "retrospect-123";
  private String userId = "user-123";
  private String goalId = "goal-123";
  private String planId = "plan-456";
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
