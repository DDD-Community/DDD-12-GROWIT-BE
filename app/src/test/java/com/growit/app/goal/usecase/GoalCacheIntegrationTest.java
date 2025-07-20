package com.growit.app.goal.usecase;

import static org.mockito.Mockito.*;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.app.user.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GoalCacheIntegrationTest {

  @Autowired GetUserGoalsUseCase getUserGoalsUseCase;

  @Autowired DeleteGoalUseCase deleteGoalUseCase;

  @MockitoBean GoalRepository goalRepository;

  User user;
  List<Goal> defaultGoals;

  @BeforeEach
  void setUp() {
    user = UserFixture.defaultUser();
    defaultGoals = List.of(GoalFixture.defaultGoal());
    when(goalRepository.findAllByUserId(user.getId())).thenReturn(defaultGoals);
    when(goalRepository.findByIdAndUserId(defaultGoals.get(0).getId(), user.getId()))
        .thenReturn(Optional.ofNullable(defaultGoals.get(0)));
  }

  @Test
  void when_userGoalsQueriedTwice_then_dbIsAccessedOnce_andSecondIsCache() {
    // 1st call: hits DB (mock)
    List<Goal> first = getUserGoalsUseCase.getMyGoals(user);
    // 2nd call: hits cache
    List<Goal> second = getUserGoalsUseCase.getMyGoals(user);

    // 검증: DB는 1번만 호출
    verify(goalRepository, times(1)).findAllByUserId(user.getId());
    assert first.equals(defaultGoals);
    assert second.equals(defaultGoals);
  }

  @Test
  void when_goalIsDeleted_then_cacheEvicted_andNextQueryHitsDbAgain() {
    getUserGoalsUseCase.getMyGoals(user);

    var deleteCommand = new DeleteGoalCommand(defaultGoals.get(0).getId(), user.getId());
    deleteGoalUseCase.execute(deleteCommand);

    getUserGoalsUseCase.getMyGoals(user);

    verify(goalRepository, times(2)).findAllByUserId(user.getId());
  }
}
