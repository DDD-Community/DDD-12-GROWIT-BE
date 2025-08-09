package com.growit.app.goal.domain.goalretrospect;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goalretrospect.GoalRetrospectFixture;
import com.growit.app.goal.domain.goalretrospect.vo.Analysis;
import org.junit.jupiter.api.Test;

class GoalRetrospectTest {

  @Test
  void givenValidParameters_whenCreateGoalRetrospect_thenGoalRetrospectCreated() {
    // given
    String goalId = "test-goal-id";
    int todoCompletedRate = 75;
    Analysis analysis = new Analysis("목표 달성률이 양호합니다", "다음 주에는 더 집중해보세요");
    String content = "이번 주 회고 내용";

    // when
    GoalRetrospect goalRetrospect =
        GoalRetrospect.create(goalId, todoCompletedRate, analysis, content);

    // then
    assertThat(goalRetrospect.getId()).isNotNull();
    assertThat(goalRetrospect.getGoalId()).isEqualTo(goalId);
    assertThat(goalRetrospect.getTodoCompletedRate()).isEqualTo(todoCompletedRate);
    assertThat(goalRetrospect.getAnalysis()).isEqualTo(analysis);
    assertThat(goalRetrospect.getContent()).isEqualTo(content);
  }

  @Test
  void givenDefaultFixture_whenCreateGoalRetrospect_thenGoalRetrospectCreated() {
    // given & when
    GoalRetrospect goalRetrospect = GoalRetrospectFixture.defaultGoalRetrospect();

    // then
    assertThat(goalRetrospect.getId()).isNotNull();
    assertThat(goalRetrospect.getGoalId()).isEqualTo("default-goal-id");
    assertThat(goalRetrospect.getTodoCompletedRate()).isEqualTo(80);
    assertThat(goalRetrospect.getAnalysis().summary()).isEqualTo("기본 요약");
    assertThat(goalRetrospect.getAnalysis().advice()).isEqualTo("기본 조언");
    assertThat(goalRetrospect.getContent()).isEqualTo("기본 회고 내용");
  }
}
