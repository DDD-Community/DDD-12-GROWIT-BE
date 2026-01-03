package com.growit.app.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.FakeAnalysisRepository;
import com.growit.app.fake.goal.FakeGoalQuery;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.dto.GoalWithAnalysisDto;
import com.growit.app.user.domain.user.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetUserGoalsUseCaseTest {

  private FakeGoalRepository fakeGoalRepository;
  private FakeAnalysisRepository fakeAnalysisRepository;
  private GetUserGoalsUseCase getUserGoalsUseCase;
  private User testUser;

  @BeforeEach
  void setUp() {
    fakeGoalRepository = new FakeGoalRepository();
    fakeAnalysisRepository = new FakeAnalysisRepository();
    FakeGoalQuery goalQuery = new FakeGoalQuery(fakeGoalRepository);
    getUserGoalsUseCase = new GetUserGoalsUseCase(goalQuery, fakeAnalysisRepository);
    testUser = UserFixture.defaultUser();
  }

  @Test
  void givenUserWithGoals_whenGetMyGoals_thenReturnGoalResponseList() {
    // given
    Goal fakeGoal = GoalFixture.defaultGoal();
    fakeGoalRepository.saveGoal(fakeGoal);

    // when
    List<GoalWithAnalysisDto> result =
        getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.PROGRESS);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getGoal()).isEqualTo(fakeGoal);
  }

  @Test
  void givenUserWithoutGoals_whenGetMyGoals_thenReturnEmptyList() {
    // when
    List<GoalWithAnalysisDto> result =
        getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.PROGRESS);

    // then
    assertThat(result).isEmpty();
  }
}
