package com.growit.app.goal.domain.goal;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.plan.Plan;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class GoalTest {

  @Test
  void givenCommand_whenUpdateGoalCommand_thenUpdated() {
    // given
    Goal original = GoalFixture.defaultGoal();
    String name = "Updated Name";
    UpdateGoalCommand command = GoalFixture.defaultUpdateGoalCommand(name, Collections.emptyList());

    // when
    original.updateByCommand(command);

    // then
    assertThat(original.getName()).isEqualTo("Updated Name");
  }

  @Test
  void givenCommand_whenUpdateByCommand_shouldUpdateMatchingPlan() {
    // given: 기존 Goal 세팅
    Goal original = GoalFixture.defaultGoal();
    Plan existingPlan = original.getPlans().get(0);
    int existingWeek = existingPlan.getWeekOfMonth();
    int newWeek = existingWeek + 1;
    String name = "Updated Name";
    UpdateGoalCommand command =
        GoalFixture.defaultUpdateGoalCommand(
            name, List.of(new PlanDto(existingWeek, "업데이트된 내용"), new PlanDto(newWeek, "새로운 계획")));

    // when
    original.updateByCommand(command);

    // then
    assertThat(original.getName()).isEqualTo("Updated Name");
    assertThat(original.getPlans().get(1).getContent()).isEqualTo("새로운 계획");
  }
}
