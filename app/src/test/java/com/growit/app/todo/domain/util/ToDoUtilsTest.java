package com.growit.app.todo.domain.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.service.ConventionCalculator;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ToDoUtilsTest {
  @Mock private ConventionCalculator conventionCalculator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void givenAllToDosCompleted_whenGetNotCompletedToDos_thenReturnsEmptyList() {
    // given
    ToDo completed1 = ToDoFixture.defaultToDo();
    completed1.updateIsCompleted(true);
    ToDo completed2 = ToDoFixture.defaultToDo();
    completed2.updateIsCompleted(true);
    List<ToDo> todos = List.of(completed1, completed2);

    // when
    List<ToDo> result = ToDoUtils.getNotCompletedToDos(todos);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void givenSomeToDosNotCompleted_whenGetNotCompletedToDos_thenReturnsOnlyNotCompleted() {
    // given
    ToDo notCompleted = ToDoFixture.defaultToDo();
    ToDo completed = ToDoFixture.defaultToDo();
    completed.updateIsCompleted(true);
    List<ToDo> todos = List.of(notCompleted, completed);

    // when
    List<ToDo> result = ToDoUtils.getNotCompletedToDos(todos);

    // then
    assertThat(result).containsExactly(notCompleted);
  }

  @Test
  void givenToDosOnSomeDays_whenGroupByDayOfWeek_thenGroupsCorrectlyAndFillsEmptyDays() {
    // given
    ToDo monday =
        ToDoFixture.customToDo(
            "monday", "user-1", LocalDate.of(2024, 7, 8), "plan-1", "goal-1"); // Monday
    ToDo tuesday =
        ToDoFixture.customToDo(
            "tuesday", "user-1", LocalDate.of(2024, 7, 9), "plan-1", "goal-1"); // Tuesday
    List<ToDo> todos = List.of(monday, tuesday);

    // when
    Map<DayOfWeek, List<ToDo>> result = ToDoUtils.groupByDayOfWeek(todos);

    // then
    assertThat(result.get(DayOfWeek.MONDAY)).containsExactly(monday);
    assertThat(result.get(DayOfWeek.TUESDAY)).containsExactly(tuesday);

    // All other days should be present and empty
    Arrays.stream(DayOfWeek.values())
        .filter(day -> day != DayOfWeek.MONDAY && day != DayOfWeek.TUESDAY)
        .forEach(day -> assertThat(result.get(day)).isEmpty());

    assertThat(result.keySet()).containsExactlyElementsOf(Arrays.asList(DayOfWeek.values()));
  }

  @Test
  void given28DayGoalAndToDos_whenGetContribution_thenReturnsStatusListOf28() {
    // given
    LocalDate startDate = LocalDate.of(2024, 7, 1);
    Goal goal =
        GoalFixture.customGoal(
            "goal-1",
            "user-1",
            "Goal",
            new GoalDuration(startDate, startDate.plusDays(27)),
            null,
            List.of(
                new Plan(
                    "plan-1", 1, "Plan", new PlanDuration(startDate, startDate.plusDays(27)))));
    ToDo completed = ToDoFixture.customToDo("todo-1", "user-1", startDate, "plan-1", "goal-1");
    completed.updateIsCompleted(true);
    ToDo notCompleted =
        ToDoFixture.customToDo("todo-2", "user-1", startDate.plusDays(1), "plan-1", "goal-1");
    notCompleted.updateIsCompleted(false);

    List<ToDo> todos = List.of(completed, notCompleted);

    System.out.println("Todos: " + todos); // 디버그 출력

    // when
    List<ToDoStatus> result = conventionCalculator.getContribution(goal, todos);

    // then
    System.out.println("Result: " + result); // 디버그 출력
    assertThat(result).hasSize(28);
    assertThat(result.get(0)).isEqualTo(ToDoStatus.COMPLETED);
    assertThat(result.get(1)).isEqualTo(ToDoStatus.NOT_STARTED);
    for (int i = 2; i < 28; i++) {
      assertThat(result.get(i)).isEqualTo(ToDoStatus.NONE);
    }
  }
}
