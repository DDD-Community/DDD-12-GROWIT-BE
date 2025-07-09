package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.service.GoalService;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetWeeklyPlanUseCaseTest {

  private ToDoRepository toDoRepository;
  private GoalService goalService;
  private GoalRepository goalRepository;
  private GetWeeklyPlanUseCase useCase;

  @BeforeEach
  void setUp() {
    toDoRepository = mock(ToDoRepository.class);
    goalService = mock(GoalService.class);
    goalRepository = mock(GoalRepository.class);
    useCase = new GetWeeklyPlanUseCase(toDoRepository, goalService, goalRepository);
  }

  @Test
  void givenValidGoalAndPlan_whenExecute_thenReturnGroupedTodosByDayOfWeek() {
    // Given
    String userId = "user-1";
    String goalId = "goal-123";
    String planId = "plan-456";
    Goal goal = mock(Goal.class);
    given(goal.getId()).willReturn(goalId);
    given(goalRepository.findById(goalId)).willReturn(Optional.of(goal));
    willDoNothing().given(goalService).checkPlanExists(userId, goalId, planId);

    ToDo mondayTodo =
        ToDoFixture.customToDo(
            "todo-1", userId, LocalDate.of(2025, 7, 8), planId, goalId); // 2025-07-08은 TUESDAY
    ToDo tuesdayTodo =
        ToDoFixture.customToDo(
            "todo-2", userId, LocalDate.of(2025, 7, 7), planId, goalId); // 2025-07-07은 MONDAY

    List<ToDo> todos = List.of(mondayTodo, tuesdayTodo);
    given(toDoRepository.findByPlanId(planId)).willReturn(todos);

    // When
    Map<DayOfWeek, List<ToDo>> result = useCase.execute(goalId, planId, userId);

    // Then
    Map<DayOfWeek, List<ToDo>> expected =
      todos.stream()
        .collect(
          Collectors.groupingBy(
            todo -> todo.getDate().getDayOfWeek(),
            LinkedHashMap::new,
            Collectors.toList()));
    for (DayOfWeek day : DayOfWeek.values()) {
      expected.putIfAbsent(day, List.of());
    }

    assertThat(result).isEqualTo(expected);
    assertThat(result.get(DayOfWeek.MONDAY)).containsExactly(tuesdayTodo);
    assertThat(result.get(DayOfWeek.TUESDAY)).containsExactly(mondayTodo);
  }

  @Test
  void givenNonexistentGoal_whenExecute_thenThrowNotFoundException() {
    // Given
    String userId = "user-1";
    String goalId = "goal-123";
    String planId = "plan-456";
    given(goalRepository.findById(goalId)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(goalId, planId, userId))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("목표를 찾을 수 없습니다.");
  }
}
