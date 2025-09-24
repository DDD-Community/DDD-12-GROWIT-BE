package com.growit.app.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendationRepository;
import com.growit.app.goal.domain.planrecommendation.dto.FindPlanRecommendationCommand;
import com.growit.app.user.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendPlanUseCaseTest {

  @Mock
  private PlanRecommendationRepository planRecommendationRepository;

  @Mock
  private GetUserGoalsUseCase getUserGoalsUseCase;

  @InjectMocks
  private RecommendPlanUseCase recommendPlanUseCase;

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
  void givenValidUserAndPlanId_whenExecute_thenReturnPlanRecommendation() {
    // given
    PlanRecommendation expectedRecommendation = new PlanRecommendation(
        "recommendation-1",
        testUser.getId(),
        testGoal.getId(),
        planId,
        "추천 내용"
    );

    when(getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.PROGRESS))
        .thenReturn(List.of(testGoal));
    when(planRecommendationRepository.findByCommand(any(FindPlanRecommendationCommand.class)))
        .thenReturn(Optional.of(expectedRecommendation));

    // when
    PlanRecommendation result = recommendPlanUseCase.execute(testUser, planId);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("recommendation-1");
    assertThat(result.getUserId()).isEqualTo(testUser.getId());
    assertThat(result.getGoalId()).isEqualTo(testGoal.getId());
    assertThat(result.getPlanId()).isEqualTo(planId);
  }

  @Test
  void givenValidUserAndPlanIdButNoRecommendation_whenExecute_thenReturnNull() {
    // given
    when(getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.PROGRESS))
        .thenReturn(List.of(testGoal));
    when(planRecommendationRepository.findByCommand(any(FindPlanRecommendationCommand.class)))
        .thenReturn(Optional.empty());

    // when
    PlanRecommendation result = recommendPlanUseCase.execute(testUser, planId);

    // then
    assertThat(result).isNull();
  }

  @Test
  void givenUserWithoutProgressGoal_whenExecute_thenThrowNotFoundException() {
    // given
    when(getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.PROGRESS))
        .thenReturn(List.of());

    // when & then
    assertThatThrownBy(() -> recommendPlanUseCase.execute(testUser, planId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("현재 진행중인 목표가 존재하지 않습니다.");
  }

  @Test
  void givenInvalidPlanId_whenExecute_thenThrowNotFoundException() {
    // given
    String invalidPlanId = "invalid-plan-id";
    
    when(getUserGoalsUseCase.getMyGoals(testUser, GoalStatus.PROGRESS))
        .thenReturn(List.of(testGoal));

    // when & then
    assertThatThrownBy(() -> recommendPlanUseCase.execute(testUser, invalidPlanId))
        .isInstanceOf(NotFoundException.class);
  }
}