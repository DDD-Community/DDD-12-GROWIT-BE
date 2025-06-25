package com.growit.app.goal.domain.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.user.domain.user.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetUserGoalsUseCaseTest {

  private FakeGoalRepository fakeGoalRepository;
  private GetUserGoalsUseCase getUserGoalsUseCase;

  private User testUser;
  private final Goal goal = GoalFixture.defaultGoal();

  @BeforeEach
  void setUp() {
    fakeGoalRepository = new FakeGoalRepository();
    getUserGoalsUseCase = new GetUserGoalsUseCase(fakeGoalRepository);
    testUser = UserFixture.defaultUser();
  }

  @Test
  void givenUserWithGoals_whenGetMyGoals_thenReturnGoalResponseList() {
    // given
    Goal fakeGoal = GoalFixture.defaultGoal();
    fakeGoalRepository.saveGoal(fakeGoal);

    // when
    List<Goal> result = getUserGoalsUseCase.getMyGoals(testUser);

    // then
    assertThat(result).containsExactly(fakeGoal);
  }

  @Test
  void givenUserWithoutGoals_whenGetMyGoals_thenReturnEmptyList() {
    // when
    List<Goal> result = getUserGoalsUseCase.getMyGoals(testUser);

    // then
    assertThat(result).isEmpty();
  }
}
