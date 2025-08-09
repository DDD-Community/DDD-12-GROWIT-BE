package com.growit.app.fake.goalretrospect;

import com.growit.app.goal.domain.goalretrospect.GoalRetrospect;
import com.growit.app.goal.domain.goalretrospect.vo.Analysis;

public class GoalRetrospectFixture {

  public static GoalRetrospect defaultGoalRetrospect() {
    return new GoalRetrospectBuilder().build();
  }

  public static GoalRetrospect withGoalId(String goalId) {
    return new GoalRetrospectBuilder().goalId(goalId).build();
  }

  public static class GoalRetrospectBuilder {
    private String goalId = "default-goal-id";
    private int todoCompletedRate = 80;
    private Analysis analysis = new Analysis("기본 요약", "기본 조언");
    private String content = "기본 회고 내용";

    public GoalRetrospectBuilder goalId(String goalId) {
      this.goalId = goalId;
      return this;
    }

    public GoalRetrospectBuilder todoCompletedRate(int todoCompletedRate) {
      this.todoCompletedRate = todoCompletedRate;
      return this;
    }

    public GoalRetrospectBuilder analysis(Analysis analysis) {
      this.analysis = analysis;
      return this;
    }

    public GoalRetrospectBuilder content(String content) {
      this.content = content;
      return this;
    }

    public GoalRetrospect build() {
      return GoalRetrospect.create(goalId, todoCompletedRate, analysis, content);
    }
  }
}