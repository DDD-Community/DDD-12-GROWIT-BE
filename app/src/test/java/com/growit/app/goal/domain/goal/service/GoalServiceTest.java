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
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.DayOfWeek;
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
  void givenStartDateNotMonday_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = next(DayOfWeek.TUESDAY);
    LocalDate end = start.plusDays(6);
    // When & Then
    assertThrows(
        BadRequestException.class,
        () -> goalService.checkGoalDuration(new GoalDuration(start, end)));
  }

  @Test
  void givenEndDateNotSunday_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = next(DayOfWeek.MONDAY);
    LocalDate end = start.plusDays(5); // Saturday
    // When & Then
    assertThrows(
        BadRequestException.class,
        () -> goalService.checkGoalDuration(new GoalDuration(start, end)));
  }

  @Test
  void givenEndDateBeforeStartDate_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = next(DayOfWeek.MONDAY).plusWeeks(1);
    LocalDate end = next(DayOfWeek.SUNDAY);
    // When & Then
    assertThrows(
        BadRequestException.class,
        () -> goalService.checkGoalDuration(new GoalDuration(start, end)));
  }

  @Test
  void givenStartDateBeforeToday_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = LocalDate.now().minusDays(7);
    while (start.getDayOfWeek() != DayOfWeek.MONDAY) {
      start = start.minusDays(1);
    }
    LocalDate end = start.plusDays(6);
    // When & Then
    LocalDate finalStart = start;
    assertThrows(
        BadRequestException.class,
        () -> goalService.checkGoalDuration(new GoalDuration(finalStart, end)));
  }

  private static LocalDate next(DayOfWeek dayOfWeek) {
    LocalDate date = LocalDate.now().plusDays(1);
    while (date.getDayOfWeek() != dayOfWeek) {
      date = date.plusDays(1);
    }
    return date;
  }

  @Test
  void givenFlexibleFirstWeek_andMondayToSundayFromSecondWeek_thenSuccess() {
    LocalDate goalStart = LocalDate.now();

    LocalDate goalEnd = goalStart.with(DayOfWeek.SUNDAY).plusWeeks(3);

    GoalDuration duration = new GoalDuration(goalStart, goalEnd);

    List<PlanDto> plans = List.of(
      new PlanDto(1, "주간계획 1"),
      new PlanDto(2, "주간계획 2"),
      new PlanDto(3, "주간계획 3"),
      new PlanDto(4, "주간계획 4")
    );

    goalService.checkPlans(duration, plans);

    for (PlanDto plan : plans) {
      PlanDuration planDuration = PlanDuration.calculateDuration(plan.weekOfMonth(), goalStart);

      if (plan.weekOfMonth() == 1) {
        assertEquals(goalStart, planDuration.startDate());
        assertEquals(DayOfWeek.SUNDAY, planDuration.endDate().getDayOfWeek());
      } else {
        assertEquals(DayOfWeek.MONDAY, planDuration.startDate().getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, planDuration.endDate().getDayOfWeek());
      }
    }
  }
}
