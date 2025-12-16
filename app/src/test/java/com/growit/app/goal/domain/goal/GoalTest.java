package com.growit.app.goal.domain.goal;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import org.junit.jupiter.api.Test;

class GoalTest {

  @Test
  void givenCommand_whenUpdateGoalCommand_thenUpdated() {
    // given
    Goal original = GoalFixture.defaultGoal();
    String name = "Updated Name";
    UpdateGoalCommand command = GoalFixture.defaultUpdateGoalCommand(name);

    // when
    original.updateGoal(command);

    // then
    assertThat(original.getName()).isEqualTo("Updated Name");
  }
}
