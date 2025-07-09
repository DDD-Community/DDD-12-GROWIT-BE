package com.growit.app.goal.domain.goal.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;
import org.junit.jupiter.api.Test;

class GoalServiceTest {

  private final GoalService goalService = new GoalService(new FakeGoalRepository());

  @Test
  void givenPlans_whenCheckPlans_thenSuccess() {
    int weeks = 4;
    GoalDuration duration = GoalFixture.createGoalDuration(weeks);
    List<PlanDto> plans = GoalFixture.createPlanDtos(weeks);

    // void 메서드지만 예외가 발생하지 않으면 성공
    goalService.checkPlans(duration, plans);
  }

  @Test
  void givenInvalidPlans_whenCheckPlans_throwBadRequestException() {
    int weeks = 4;
    GoalDuration duration = GoalFixture.createGoalDuration(weeks);
    List<PlanDto> plans = List.of(); // 계획이 없음

    assertThrows(BadRequestException.class, () -> goalService.checkPlans(duration, plans));
  }

  @Test
  void givenInvalidUser_whenCheckMyGoal_throwBadRequestException() {
    Goal goal = GoalFixture.defaultGoal();
    assertThrows(BadRequestException.class, () -> goalService.checkMyGoal(goal, "otherUser"));
  }

  @Test
  void givenValidUser_whenCheckPlanExists_throwBadRequestException() {
    Goal goal = GoalFixture.defaultGoal();
    assertThrows(
        NotFoundException.class,
        () -> goalService.checkPlanExists(goal.getUserId(), goal.getId(), "notExistPlanId"));
  }
}
