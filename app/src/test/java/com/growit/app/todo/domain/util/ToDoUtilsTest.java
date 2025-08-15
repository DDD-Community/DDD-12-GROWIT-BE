package com.growit.app.todo.domain.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.domain.ToDo;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class ToDoUtilsTest {

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
}
