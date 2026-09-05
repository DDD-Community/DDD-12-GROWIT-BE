package com.growit.app.todo.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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
import com.growit.app.todo.domain.vo.ToDoCategory;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
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

    routine = Routine.of(duration, RepeatType.DAILY, null);

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
    // Given
    // setUp()에서 이미 daily routine이 준비됨

    // When
    ToDoResult result = routineService.createRoutineToDos(createCommand);

    // Then
    verify(toDoRepository, times(7)).saveToDo(any(ToDo.class)); // 7일간의 daily routine
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
  }

  @Test
  @DisplayName("루틴 생성 시 선택한 카테고리가 모든 회차에 유지되어야 한다")
  void shouldPreserveCategoryForEveryRoutineOccurrence() {
    CreateToDoCommand command =
        new CreateToDoCommand(
            "user123",
            "goal123",
            "Daily routine task",
            LocalDate.of(2024, 1, 1),
            false,
            ToDoCategory.DELETE,
            routine);
    ArgumentCaptor<ToDo> captor = ArgumentCaptor.forClass(ToDo.class);

    routineService.createRoutineToDos(command);

    verify(toDoRepository, times(7)).saveToDo(captor.capture());
    assertThat(captor.getAllValues()).allMatch(todo -> todo.getCategory() == ToDoCategory.DELETE);
  }

  @Test
  @DisplayName("FROM_DATE 타입 수정에서 일정이 그대로면 삭제 없이 제자리 수정한다")
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

    // Then: 반복 주기·기간·날짜가 그대로면 지우고 다시 만들지 않고 제자리에서 내용만 바꾼다.
    verify(toDoRepository, never()).deleteToDo(anyString());
    verify(toDoRepository, times(3)).saveToDo(any(ToDo.class));
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("ALL 타입 수정에서 일정이 그대로면 삭제 없이 제자리 수정한다")
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

    // Then: FROM_DATE 와 같은 이유로 제자리 수정된다.
    verify(toDoRepository, never()).deleteToDo(anyString());
    verify(toDoRepository, times(3)).saveToDo(any(ToDo.class));
    assertThat(result).isNotNull();
  }

  /**
   * FakeToDoRepository 는 저장한 ToDo 인스턴스를 그대로 들고 있어, 도메인 객체를 바꾸기만 하고 saveToDo 를 호출하지 않아도 조회 결과가 바뀐
   * 것처럼 보인다. 그래서 "저장했는가"는 mock 으로만 검증할 수 있다.
   */
  @Test
  @DisplayName("SINGLE 수정은 변경된 ToDo 를 실제로 저장한다")
  void shouldPersistSingleUpdate() {
    UpdateToDoCommand singleCommand =
        new UpdateToDoCommand(
            "todo123",
            "user123",
            "goal123",
            "Updated single task",
            LocalDate.of(2024, 1, 3),
            true,
            routine,
            RoutineUpdateType.SINGLE);

    routineService.updateRoutineToDos(existingToDo, singleCommand);

    verify(toDoRepository, times(1)).saveToDo(existingToDo);
  }

  @Test
  @DisplayName("시리즈를 나눌 때 앞쪽 회차를 좁힌 반복으로 실제로 다시 저장한다")
  void shouldPersistNarrowedPrecedingSeries() {
    // 요일을 바꿔 재생성 경로로 보낸다.
    Routine wednesday =
        Routine.of(
            RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7)),
            RepeatType.WEEKLY,
            List.of(DayOfWeek.WEDNESDAY));

    UpdateToDoCommand splitCommand =
        new UpdateToDoCommand(
            "todo123",
            "user123",
            "goal123",
            "Split",
            LocalDate.of(2024, 1, 3),
            true,
            wednesday,
            RoutineUpdateType.FROM_DATE);

    ToDo preceding = createToDo("todo-before", LocalDate.of(2024, 1, 1));
    given(toDoRepository.findByRoutineIdAndUserId(anyString(), anyString()))
        .willReturn(List.of(preceding));
    given(toDoRepository.findByRoutineIdAndUserIdAndDateAfter(anyString(), anyString(), any()))
        .willReturn(List.of(createToDo("todo-after", LocalDate.of(2024, 1, 3))));

    routineService.updateRoutineToDos(existingToDo, splitCommand);

    // 앞쪽 회차가 좁힌 반복으로 다시 저장되지 않으면, 나중에 앞쪽에서 ALL 을 누를 때 회차가 중복된다.
    verify(toDoRepository).saveToDo(preceding);
    assertThat(preceding.getRoutine().getDuration().getEndDate())
        .isEqualTo(LocalDate.of(2024, 1, 2));
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

    Routine weeklyRoutine = Routine.of(duration, RepeatType.WEEKLY, null);

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

  @Test
  @DisplayName("주간 반복에서 지정된 요일에만 ToDo가 생성되어야 한다")
  void shouldCreateWeeklyToDosOnSpecificDays() {
    // Given
    RoutineDuration duration =
        RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 14)); // 2주간

    List<DayOfWeek> repeatDays =
        Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
    Routine weeklyRoutineWithDays = Routine.of(duration, RepeatType.WEEKLY, repeatDays);

    CreateToDoCommand weeklyCommand =
        new CreateToDoCommand(
            "user123",
            "goal123",
            "Weekly routine on specific days",
            LocalDate.of(2024, 1, 1), // 월요일
            true,
            weeklyRoutineWithDays);

    // When
    ToDoResult result = routineService.createRoutineToDos(weeklyCommand);

    // Then
    // 2주간 월/수/금만 생성 = 6개 ToDo
    verify(toDoRepository, times(6)).saveToDo(any(ToDo.class));
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
  }

  @Test
  @DisplayName("격주 반복에서 지정된 요일에만 ToDo가 생성되어야 한다")
  void shouldCreateBiweeklyToDosOnSpecificDays() {
    // Given
    RoutineDuration duration =
        RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 28)); // 4주간

    List<DayOfWeek> repeatDays = Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY);
    Routine biweeklyRoutine = Routine.of(duration, RepeatType.BIWEEKLY, repeatDays);

    CreateToDoCommand biweeklyCommand =
        new CreateToDoCommand(
            "user123",
            "goal123",
            "Biweekly routine on specific days",
            LocalDate.of(2024, 1, 1), // 월요일
            true,
            biweeklyRoutine);

    // When
    ToDoResult result = routineService.createRoutineToDos(biweeklyCommand);

    // Then
    // 격주(0주차, 2주차)에서 화/목만 생성 = 4개 ToDo
    verify(toDoRepository, times(4)).saveToDo(any(ToDo.class));
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
  }

  @Test
  @DisplayName("repeatDays가 null이면 기존 로직으로 동작해야 한다")
  void shouldUseOriginalLogicWhenRepeatDaysIsNull() {
    // Given
    RoutineDuration duration =
        RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15)); // 2주간

    Routine weeklyRoutineWithoutDays = Routine.of(duration, RepeatType.WEEKLY, null);

    CreateToDoCommand weeklyCommand =
        new CreateToDoCommand(
            "user123",
            "goal123",
            "Weekly routine without specific days",
            LocalDate.of(2024, 1, 1), // 월요일
            true,
            weeklyRoutineWithoutDays);

    // When
    ToDoResult result = routineService.createRoutineToDos(weeklyCommand);

    // Then
    // 기존 주간 반복 로직: 매주 월요일 = 3개 ToDo (1/1, 1/8, 1/15)
    verify(toDoRepository, times(3)).saveToDo(any(ToDo.class));
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
  }

  @Test
  @DisplayName("일간 반복은 repeatDays와 관계없이 매일 ToDo가 생성되어야 한다")
  void shouldCreateDailyToDosRegardlessOfRepeatDays() {
    // Given
    RoutineDuration duration =
        RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)); // 5일간

    List<DayOfWeek> repeatDays = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    Routine dailyRoutineWithDays = Routine.of(duration, RepeatType.DAILY, repeatDays);

    CreateToDoCommand dailyCommand =
        new CreateToDoCommand(
            "user123",
            "goal123",
            "Daily routine with repeatDays",
            LocalDate.of(2024, 1, 1),
            true,
            dailyRoutineWithDays);

    // When
    ToDoResult result = routineService.createRoutineToDos(dailyCommand);

    // Then
    // 일간 반복은 repeatDays 무시하고 매일 생성 = 5개 ToDo
    verify(toDoRepository, times(5)).saveToDo(any(ToDo.class));
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
  }
}
