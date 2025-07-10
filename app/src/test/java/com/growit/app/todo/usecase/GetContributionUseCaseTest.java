package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.util.ToDoUtils;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class GetContributionUseCaseTest {

  @Mock private GoalRepository goalRepository;
  @Mock private ToDoRepository toDoRepository;
  @InjectMocks private GetContributionUseCase getContributionUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void givenExistingGoalAndToDos_whenExecute_thenReturnsContributionStatusList() {
    // given
    String userId = "user-1";
    String goalId = "goal-1";
    String planId1 = "plan-1";
    String planId2 = "plan-2";

    Plan plan1 = mock(Plan.class);
    when(plan1.getId()).thenReturn(planId1);
    Plan plan2 = mock(Plan.class);
    when(plan2.getId()).thenReturn(planId2);
    Goal goal = mock(Goal.class);
    when(goal.getPlans()).thenReturn(List.of(plan1, plan2));

    when(goalRepository.findByIdAndUserId(goalId, userId)).thenReturn(Optional.of(goal));

    ToDo todo1 = mock(ToDo.class);
    ToDo todo2 = mock(ToDo.class);
    List<ToDo> toDoList = List.of(todo1, todo2);
    when(toDoRepository.findByPlanIdIn(List.of(planId1, planId2))).thenReturn(toDoList);

    try (MockedStatic<ToDoUtils> utils = Mockito.mockStatic(ToDoUtils.class)) {
      List<ToDoStatus> mockResult = List.of(ToDoStatus.COMPLETED, ToDoStatus.NOT_STARTED);
      utils.when(() -> ToDoUtils.getContribution(goal, toDoList)).thenReturn(mockResult);

      // when
      List<ToDoStatus> result = getContributionUseCase.execute(userId, goalId);

      // then
      assertThat(result).containsExactly(ToDoStatus.COMPLETED, ToDoStatus.NOT_STARTED);
      verify(goalRepository).findByIdAndUserId(goalId, userId);
      verify(toDoRepository).findByPlanIdIn(List.of(planId1, planId2));
    }
  }

  @Test
  void givenGoalNotFound_whenExecute_thenThrowsException() {
    // given
    String userId = "user-1";
    String goalId = "goal-x";
    when(goalRepository.findByIdAndUserId(goalId, userId)).thenReturn(Optional.empty());

    // when & then
    Assertions.assertThrows(
        java.util.NoSuchElementException.class,
        () -> getContributionUseCase.execute(userId, goalId));
    verify(goalRepository).findByIdAndUserId(goalId, userId);
    verifyNoInteractions(toDoRepository);
  }
}
