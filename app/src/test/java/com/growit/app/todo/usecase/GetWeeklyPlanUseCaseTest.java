package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.service.GoalService;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  void given_validGoalAndPlan_when_execute_then_returnGroupedTodosByDayOfWeek() {
    // given
    String userId = "user-1";
    String goalId = "goal-123";
    String planId = "plan-456";
    Goal goal = mock(Goal.class);
    given(goal.getId()).willReturn(goalId);
    given(goalRepository.findById(goalId)).willReturn(Optional.of(goal));
    willDoNothing().given(goalService).checkPlanExists(userId, goalId, planId);

    ToDo todo = mock(ToDo.class);
    Map<DayOfWeek, List<ToDo>> grouped =
        Map.of(
            DayOfWeek.MONDAY, List.of(todo),
            DayOfWeek.TUESDAY, List.of());
    given(toDoRepository.groupByPlanId(userId, goalId, planId)).willReturn(grouped);

    // when
    Map<DayOfWeek, List<ToDo>> result = useCase.execute(goalId, planId, userId);

    // then
    assertThat(result).isEqualTo(grouped);
  }

  @Test
  void given_nonexistentGoal_when_execute_then_throwNotFoundException() {
    // given
    String userId = "user-1";
    String goalId = "goal-123";
    String planId = "plan-456";
    given(goalRepository.findById(goalId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> useCase.execute(goalId, planId, userId))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("목표를 찾을 수 없습니다.");
  }
}
