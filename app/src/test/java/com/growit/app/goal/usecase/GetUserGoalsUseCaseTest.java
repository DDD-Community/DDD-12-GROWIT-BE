package com.growit.app.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.user.domain.user.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetUserGoalsUseCaseTest {

  private FakeGoalRepository fakeGoalRepository;
  private GetUserGoalsUseCase getUserGoalsUseCase;
  private User testUser;

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
    List<Goal> result = getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.NONE);

    // then
    assertThat(result).containsExactly(fakeGoal);
  }

  @Test
  void givenUserWithoutGoals_whenGetMyGoals_thenReturnEmptyList() {
    // when
    List<Goal> result = getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.NONE);

    // then
    assertThat(result).isEmpty();
  }
}
