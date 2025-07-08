package com.growit.app.goal.domain.goal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoalServiceTest {

  private final GoalRepository goalRepository = new FakeGoalRepository();
  private final Goal goal = GoalFixture.defaultGoal();

  private final GoalService goalService = new GoalService(goalRepository);

  @BeforeEach
  void setUp() {
    goalRepository.saveGoal(goal);
  }

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
  void givenValidUser_whenCheckPlanExists_throwBadRequestException() {
    Goal goal = GoalFixture.defaultGoal();
    assertThrows(
        NotFoundException.class,
        () -> goalService.checkPlanExists(goal.getUserId(), goal.getId(), "notExistPlanId"));
  }

  @Test
  void givenValidGoalIdAndUserId_whenGetMyGoal_thenReturnGoal() {
    // given
    String id = goal.getId();
    String userId = goal.getUserId();

    // when
    Goal result = goalService.getMyGoal(id, userId);

    // then
    assertEquals(goal.getId(), result.getId());
  }

  @Test
  void givenInvalidGoalId_whenGetMyGoal_thenThrowNotFoundException() {
    // given
    String id = "invalidId";
    String userId = goal.getUserId();

    // when & then
    assertThrows(
        NotFoundException.class,
        () -> {
          goalService.getMyGoal(id, userId);
        });
  }

  @Test
  void givenInvalidUserId_whenGetMyGoal_thenThrowNotFoundException() {
    // given
    String id = goal.getUserId();
    String userId = "invalidUserId";

    // when & then
    assertThrows(
        NotFoundException.class,
        () -> {
          goalService.getMyGoal(id, userId);
        });
  }
}
