package com.growit.app.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationDataCollector;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationService;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
import com.growit.app.user.domain.user.User;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendPlanUseCaseTest {

  @Mock private GoalRecommendationDataCollector dataCollector;

  @Mock private GoalRecommendationService recommendationService;

  @Mock private GetGoalUseCase getGoalUseCase;

  @InjectMocks private RecommendPlanUseCase recommendPlanUseCase;

  private User testUser;
  private Goal testGoal;
  private String planId;

  @BeforeEach
  void setUp() {
    testUser = UserFixture.defaultUser();
    testGoal = GoalFixture.defaultGoal();
    planId = "plan-1";
  }

  @Test
  void givenValidUserAndPlanId_whenExecute_thenExecuteSuccessfully() {
    // given
    GoalRecommendationData mockData =
        GoalRecommendationData.builder()
            .pastTodos(Arrays.asList("todo1", "todo2"))
            .completedTodos(Arrays.asList("completed1"))
            .pastRetrospects(Arrays.asList("retrospect1"))
            .pastWeeklyGoals(Arrays.asList("weekly1"))
            .remainingTime("7일")
            .build();

    when(getGoalUseCase.getGoal(testGoal.getId(), testUser)).thenReturn(testGoal);
    when(dataCollector.collectData(testUser, testGoal)).thenReturn(mockData);

    // when & then - should complete without exception
    recommendPlanUseCase.execute(testUser, testGoal.getId(), planId);
  }

  @Test
  void givenValidUserAndPlanId_whenExecute_thenCollectDataAndGenerateRecommendation() {
    // given
    GoalRecommendationData mockData =
        GoalRecommendationData.builder()
            .pastTodos(Arrays.asList("todo1", "todo2"))
            .completedTodos(Arrays.asList("completed1", "completed2"))
            .pastRetrospects(Arrays.asList("retrospect1"))
            .pastWeeklyGoals(Arrays.asList("weekly1"))
            .remainingTime("14일")
            .build();

    when(getGoalUseCase.getGoal(testGoal.getId(), testUser)).thenReturn(testGoal);
    when(dataCollector.collectData(testUser, testGoal)).thenReturn(mockData);

    // when & then - should complete without exception
    recommendPlanUseCase.execute(testUser, testGoal.getId(), planId);
  }

  @Test
  void givenUserWithoutProgressGoal_whenExecute_thenThrowNotFoundException() {
    // given
    when(getGoalUseCase.getGoal(testGoal.getId(), testUser))
        .thenThrow(new NotFoundException("현재 진행중인 목표가 존재하지 않습니다."));

    // when & then
    assertThatThrownBy(() -> recommendPlanUseCase.execute(testUser, testGoal.getId(), planId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("현재 진행중인 목표가 존재하지 않습니다.");
  }

  @Test
  void givenInvalidPlanId_whenExecute_thenThrowNotFoundException() {
    // given
    String invalidPlanId = "invalid-plan-id";

    when(getGoalUseCase.getGoal(testGoal.getId(), testUser)).thenReturn(testGoal);

    // when & then
    assertThatThrownBy(
            () -> recommendPlanUseCase.execute(testUser, testGoal.getId(), invalidPlanId))
        .isInstanceOf(NotFoundException.class);
  }
}
