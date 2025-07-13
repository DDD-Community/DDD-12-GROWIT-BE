package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.service.ConventionCalculator;
import com.growit.app.todo.domain.util.ToDoUtils;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class GetContributionUseCaseTest {

  @Mock private ToDoRepository toDoRepository;
  @Mock private GoalQuery goalQuery;
  @Mock private ConventionCalculator conventionCalculator;
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
    Goal goal = GoalFixture.defaultGoal();

    when(goalQuery.getMyGoal(goalId, userId)).thenReturn(goal);

    ToDo todo1 = ToDoFixture.defaultToDo();
    ToDo todo2 = ToDoFixture.customToDo("todo-2", null, null, null, goalId);
    List<ToDo> toDoList = List.of(todo1, todo2);
    when(toDoRepository.findByGoalId(goalId)).thenReturn(toDoList);

    try (MockedStatic<ToDoUtils> utils = Mockito.mockStatic(ToDoUtils.class)) {
      List<ToDoStatus> mockResult = List.of(ToDoStatus.COMPLETED, ToDoStatus.NOT_STARTED);
      utils.when(() -> conventionCalculator.getContribution(goal, toDoList)).thenReturn(mockResult);

      // when
      List<ToDoStatus> result = getContributionUseCase.execute(userId, goalId);

      // then
      assertThat(result).containsExactly(ToDoStatus.COMPLETED, ToDoStatus.NOT_STARTED);
      verify(goalQuery).getMyGoal(goalId, userId);
      verify(toDoRepository).findByGoalId(goalId);
    }
  }

  @Test
  void givenGoalNotFound_whenExecute_thenThrowsException() {
    // given
    String userId = "user-1";
    String goalId = "goal-x";
    when(goalQuery.getMyGoal(goalId, userId)).thenThrow(new java.util.NoSuchElementException());

    // when & then
    org.junit.jupiter.api.Assertions.assertThrows(
        java.util.NoSuchElementException.class,
        () -> getContributionUseCase.execute(userId, goalId));
    verify(goalQuery).getMyGoal(goalId, userId);
    verifyNoInteractions(toDoRepository);
  }
}
