package com.growit.app.todo.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDuration;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

  @Mock private ToDoRepository toDoRepository;

  @InjectMocks private RoutineServiceImpl routineService;

  private CreateToDoCommand createCommand;
  private UpdateToDoCommand updateCommand;
  private ToDo existingToDo;
  private Routine routine;

  @BeforeEach
  void setUp() {
    RoutineDuration duration =
        RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7));

    routine = Routine.of(duration, RepeatType.DAILY);

    createCommand =
        new CreateToDoCommand(
            "user123", "goal123", "Daily routine task", LocalDate.of(2024, 1, 1), true, routine);

    updateCommand =
        new UpdateToDoCommand(
            "todo123",
            "user123",
            "goal123",
            "Updated routine task",
            LocalDate.of(2024, 1, 3),
            true,
            routine,
            RoutineUpdateType.FROM_DATE);

    existingToDo =
        ToDo.builder()
            .id("todo123")
            .userId("user123")
            .goalId("goal123")
            .content("Existing task")
            .date(LocalDate.of(2024, 1, 3))
            .isCompleted(false)
            .isDeleted(false)
            .isImportant(true)
            .routine(routine)
            .build();
  }

  @Test
  @DisplayName("루틴 생성 시 반복 타입에 따라 여러 ToDo가 생성되어야 한다")
  void shouldCreateMultipleToDosForRoutine() {
    // When
    ToDoResult result = routineService.createRoutineToDos(createCommand);

    // Then
    verify(toDoRepository, times(7)).saveToDo(any(ToDo.class)); // 7일간의 daily routine
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
  }

  @Test
  @DisplayName("FROM_DATE 타입으로 루틴 수정 시 해당 날짜 이후 ToDo들이 삭제되고 새로 생성되어야 한다")
  void shouldUpdateRoutineFromDate() {
    // Given
    List<ToDo> existingToDos =
        Arrays.asList(
            createToDo("todo1", LocalDate.of(2024, 1, 3)),
            createToDo("todo2", LocalDate.of(2024, 1, 4)),
            createToDo("todo3", LocalDate.of(2024, 1, 5)));
    given(toDoRepository.findByRoutineIdAndUserIdAndDateAfter(anyString(), anyString(), any()))
        .willReturn(existingToDos);

    // When
    ToDoResult result = routineService.updateRoutineToDos(existingToDo, updateCommand);

    // Then
    verify(toDoRepository, times(3)).deleteToDo(anyString()); // 기존 ToDo 삭제
    verify(toDoRepository, times(5)).saveToDo(any(ToDo.class)); // 새로운 ToDo 생성 (1/3~1/7)
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("ALL 타입으로 루틴 수정 시 모든 루틴 ToDo가 삭제되고 새로 생성되어야 한다")
  void shouldUpdateAllRoutineToDos() {
    // Given
    UpdateToDoCommand allUpdateCommand =
        new UpdateToDoCommand(
            "todo123",
            "user123",
            "goal123",
            "Updated all routine tasks",
            LocalDate.of(2024, 1, 3),
            true,
            routine,
            RoutineUpdateType.ALL);

    List<ToDo> allToDos =
        Arrays.asList(
            createToDo("todo1", LocalDate.of(2024, 1, 1)),
            createToDo("todo2", LocalDate.of(2024, 1, 2)),
            createToDo("todo3", LocalDate.of(2024, 1, 3)));
    given(toDoRepository.findByRoutineIdAndUserId(anyString(), anyString())).willReturn(allToDos);

    // When
    ToDoResult result = routineService.updateRoutineToDos(existingToDo, allUpdateCommand);

    // Then
    verify(toDoRepository, times(3)).deleteToDo(anyString()); // 모든 기존 ToDo 삭제
    verify(toDoRepository, times(7)).saveToDo(any(ToDo.class)); // 새로운 루틴 생성
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("SINGLE 타입으로 루틴 수정 시 해당 ToDo만 수정되어야 한다")
  void shouldUpdateSingleToDo() {
    // Given
    UpdateToDoCommand singleUpdateCommand =
        new UpdateToDoCommand(
            "todo123",
            "user123",
            "goal123",
            "Updated single task",
            LocalDate.of(2024, 1, 3),
            true,
            null, // 루틴 제거
            RoutineUpdateType.SINGLE);

    // When
    ToDoResult result = routineService.updateRoutineToDos(existingToDo, singleUpdateCommand);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("todo123");
  }

  @Test
  @DisplayName("주간 반복 루틴 생성 시 올바른 간격으로 ToDo가 생성되어야 한다")
  void shouldCreateWeeklyRoutineToDos() {
    // Given
    RoutineDuration duration =
        RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 29));

    Routine weeklyRoutine = Routine.of(duration, RepeatType.WEEKLY);

    CreateToDoCommand weeklyCommand =
        new CreateToDoCommand(
            "user123",
            "goal123",
            "Weekly routine task",
            LocalDate.of(2024, 1, 1),
            true,
            weeklyRoutine);

    // When
    ToDoResult result = routineService.createRoutineToDos(weeklyCommand);

    // Then
    verify(toDoRepository, times(5)).saveToDo(any(ToDo.class)); // 5주간의 weekly routine
    assertThat(result).isNotNull();
  }

  private ToDo createToDo(String id, LocalDate date) {
    return ToDo.builder()
        .id(id)
        .userId("user123")
        .goalId("goal123")
        .content("Task " + id)
        .date(date)
        .isCompleted(false)
        .isDeleted(false)
        .isImportant(false)
        .routine(routine)
        .build();
  }
}
