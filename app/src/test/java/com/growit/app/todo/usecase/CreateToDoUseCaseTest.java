package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.service.RoutineService;
import com.growit.app.todo.domain.service.ToDoHandler;
import com.growit.app.todo.domain.service.ToDoValidator;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDuration;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateToDoUseCaseTest {

  @Mock private GoalQuery goalQuery;
  @Mock private ToDoHandler toDoHandler;
  @Mock private ToDoValidator toDoValidator;
  @Mock private ToDoRepository toDoRepository;
  @Mock private RoutineService routineService;

  @InjectMocks private CreateToDoUseCase createToDoUseCase;

  private Goal goal;
  private CreateToDoCommand simpleCommand;
  private CreateToDoCommand routineCommand;

  @BeforeEach
  void setUp() {
    goal = GoalFixture.customGoal("goal123", "Test Goal", null);

    simpleCommand =
        new CreateToDoCommand("user123", "goal123", "Simple task", LocalDate.now(), false, null);

    RoutineDuration duration =
        RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7));

    Routine routine = Routine.of(duration, RepeatType.DAILY, null);

    routineCommand =
        new CreateToDoCommand(
            "user123", "goal123", "Routine task", LocalDate.of(2024, 1, 1), true, routine);
  }

  @Test
  @DisplayName("일반 ToDo 생성 시 정상적으로 처리되어야 한다")
  void shouldCreateSimpleToDo() {
    // Given
    given(goalQuery.getMyGoal("goal123", "user123")).willReturn(goal);

    // When
    ToDoResult result = createToDoUseCase.execute(simpleCommand);

    // Then
    verify(goalQuery).getMyGoal("goal123", "user123");
    verify(toDoValidator).tooManyToDoCreated(any(), any(), any());
    verify(toDoHandler).handle("goal123");
    verify(toDoRepository).saveToDo(any(ToDo.class));
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
  }

  @Test
  @DisplayName("루틴 ToDo 생성 시 RoutineService를 호출해야 한다")
  void shouldCreateRoutineToDo() {
    // Given
    given(goalQuery.getMyGoal("goal123", "user123")).willReturn(goal);
    given(routineService.createRoutineToDos(routineCommand))
        .willReturn(new ToDoResult("routine-todo-123"));

    // When
    ToDoResult result = createToDoUseCase.execute(routineCommand);

    // Then
    verify(goalQuery).getMyGoal("goal123", "user123");
    verify(toDoValidator).tooManyToDoCreated(any(), any(), any());
    verify(toDoHandler).handle("goal123");
    verify(routineService).createRoutineToDos(routineCommand);
    verify(toDoRepository, times(0)).saveToDo(any(ToDo.class)); // 루틴 서비스에서 처리되므로 직접 호출되지 않음
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("routine-todo-123");
  }
}
