package com.growit.app.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalService;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.user.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetGoalsByYearUseCaseTest {

  private FakeGoalRepository fakeGoalRepository;
  private GoalService goalService;
  private GetGoalsByYearUseCase getGoalsByYearUseCase;
  private User testUser;

  @BeforeEach
  void setUp() {
    fakeGoalRepository = new FakeGoalRepository();
    goalService = new GoalService(fakeGoalRepository);
    getGoalsByYearUseCase = new GetGoalsByYearUseCase(goalService);
    testUser = UserFixture.defaultUser();
  }

  @Test
  void givenUserWithGoalsInSpecificYear_whenGetGoalsByYear_thenReturnGoalsFromThatYear() {
    // given
    LocalDate date2024 = LocalDate.of(2024, 1, 1);
    LocalDate date2025 = LocalDate.of(2025, 1, 1);

    Goal goal2024 =
        GoalFixture.customGoal(
            "goal-2024",
            testUser.getId(),
            "2024 목표",
            new GoalDuration(date2024, date2024.plusWeeks(4)),
            null,
            null,
            null,
            null,
            null);

    Goal goal2025 =
        GoalFixture.customGoal(
            "goal-2025",
            testUser.getId(),
            "2025 목표",
            new GoalDuration(date2025, date2025.plusWeeks(4)),
            null,
            null,
            null,
            null,
            null);

    fakeGoalRepository.saveGoal(goal2024);
    fakeGoalRepository.saveGoal(goal2025);

    // when
    List<Goal> result2024 = getGoalsByYearUseCase.getGoalsByYear(testUser, 2024);
    List<Goal> result2025 = getGoalsByYearUseCase.getGoalsByYear(testUser, 2025);

    // then
    assertThat(result2024).hasSize(1);
    assertThat(result2024.get(0).getName()).isEqualTo("2024 목표");

    assertThat(result2025).hasSize(1);
    assertThat(result2025.get(0).getName()).isEqualTo("2025 목표");
  }

  @Test
  void givenUserWithoutGoalsInSpecificYear_whenGetGoalsByYear_thenReturnEmptyList() {
    // given
    LocalDate date2024 = LocalDate.of(2024, 1, 1);
    Goal goal2024 =
        GoalFixture.customGoal(
            "goal-2024",
            testUser.getId(),
            "2024 목표",
            new GoalDuration(date2024, date2024.plusWeeks(4)),
            null,
            null,
            null,
            null,
            null);
    fakeGoalRepository.saveGoal(goal2024);

    // when
    List<Goal> result = getGoalsByYearUseCase.getGoalsByYear(testUser, 2025);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void givenMultipleGoalsInSameYear_whenGetGoalsByYear_thenReturnAllGoalsSortedByStartDateDesc() {
    // given
    LocalDate earlyDate = LocalDate.of(2024, 1, 1);
    LocalDate lateDate = LocalDate.of(2024, 6, 1);

    Goal earlyGoal =
        GoalFixture.customGoal(
            "early-goal",
            testUser.getId(),
            "Early Goal",
            new GoalDuration(earlyDate, earlyDate.plusWeeks(4)),
            null,
            null,
            null,
            null,
            null);

    Goal lateGoal =
        GoalFixture.customGoal(
            "late-goal",
            testUser.getId(),
            "Late Goal",
            new GoalDuration(lateDate, lateDate.plusWeeks(4)),
            null,
            null,
            null,
            null,
            null);

    fakeGoalRepository.saveGoal(earlyGoal);
    fakeGoalRepository.saveGoal(lateGoal);

    // when
    List<Goal> result = getGoalsByYearUseCase.getGoalsByYear(testUser, 2024);

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("Late Goal");
    assertThat(result.get(1).getName()).isEqualTo("Early Goal");
  }
}
