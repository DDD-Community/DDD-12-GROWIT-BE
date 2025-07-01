package com.growit.app.goal.domain.goal.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class GoalServiceTest {

  private final GoalService goalService = new GoalService();

  @Test
  void givenPlans_whenCheckPlans_thenSuccess() {
    LocalDate start = LocalDate.of(2025, 6, 23);
    LocalDate end = LocalDate.of(2025, 7, 6);
    GoalDuration duration = new GoalDuration(start, end);
    List<PlanDto> plans = List.of(new PlanDto(1, "주간계획 1"), new PlanDto(2, "주간계획 2"));

    // void 메서드지만 예외가 발생하지 않으면 성공
    goalService.checkPlans(duration, plans);
  }

  @Test
  void givenInvalidPlans_whenCheckPlans_throwBadRequestException() {
    LocalDate start = LocalDate.of(2025, 6, 23);
    LocalDate end = LocalDate.of(2025, 7, 6);
    GoalDuration duration = new GoalDuration(start, end);
    List<PlanDto> plans = List.of(); // 계획이 없음

    assertThrows(BadRequestException.class, () -> goalService.checkPlans(duration, plans));
  }

  @Test
  void givenInvalidUser_whenCheckMyGoal_throwBadRequestException() {
    Goal goal = GoalFixture.defaultGoal();
    assertThrows(BadRequestException.class, () -> goalService.checkMyGoal(goal, "otherUser"));
  }
}
