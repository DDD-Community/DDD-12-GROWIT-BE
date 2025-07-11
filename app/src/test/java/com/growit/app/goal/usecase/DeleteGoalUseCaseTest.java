package com.growit.app.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.FakeGoalQuery;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteGoalUseCaseTest {

  private FakeGoalRepository goalRepository;
  private DeleteGoalUseCase deleteGoalUseCase;

  @BeforeEach
  void setUp() {
    goalRepository = new FakeGoalRepository();
    deleteGoalUseCase = new DeleteGoalUseCase(goalRepository, new FakeGoalQuery(goalRepository));
  }

  @Test
  void execute_success() {
    // given
    Goal goal = GoalFixture.defaultGoal();
    goalRepository.saveGoal(goal);

    DeleteGoalCommand command = new DeleteGoalCommand(goal.getId(), "user-1");

    // when
    assertDoesNotThrow(() -> deleteGoalUseCase.execute(command));

    // then
    Goal deletedGoal = goalRepository.findById(goal.getId()).orElseThrow();
    assertTrue(deletedGoal.getDeleted());
  }

  @Test
  void execute_goalNotFound() {
    // given
    DeleteGoalCommand command = new DeleteGoalCommand("not_exist_id", "user-1");

    // when & then
    assertThrows(NotFoundException.class, () -> deleteGoalUseCase.execute(command));
  }

  @Test
  void execute_invalidUser() {
    // given
    Goal goal = GoalFixture.defaultGoal();
    goalRepository.saveGoal(goal);

    DeleteGoalCommand command = new DeleteGoalCommand(goal.getId(), "user-2"); // 다른 유저

    // when & then
    assertThrows(NotFoundException.class, () -> deleteGoalUseCase.execute(command));
  }
}
