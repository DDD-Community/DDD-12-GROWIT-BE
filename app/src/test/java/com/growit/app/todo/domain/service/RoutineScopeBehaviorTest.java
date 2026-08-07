package com.growit.app.todo.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.DeleteToDoCommand;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDeleteType;
import com.growit.app.todo.domain.vo.RoutineDuration;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 반복 투두 범위 선택(SINGLE / FROM_DATE / ALL)의 실제 결과를 검증한다.
 *
 * <p>호출 횟수(verify times)가 아니라 <b>저장소에 남은 투두의 날짜·내용·완료여부</b>를 단언한다. 기존 RoutineServiceTest 가 호출 횟수만
 * 검증한 탓에 기준 날짜 버그와 완료 이력 소실이 모두 통과했기 때문이다.
 *
 * <p>시나리오: 매주 월요일 반복 — 2024-01-01, 01-08, 01-15, 01-22
 */
class RoutineScopeBehaviorTest {

  private static final String USER_ID = "user-1";
  private static final String GOAL_ID = "goal-1";
  private static final String OLD_CONTENT = "기존 내용";
  private static final String NEW_CONTENT = "변경된 내용";

  private static final LocalDate WEEK_1 = LocalDate.of(2024, 1, 1); // 월
  private static final LocalDate WEEK_2 = LocalDate.of(2024, 1, 8); // 월
  private static final LocalDate WEEK_3 = LocalDate.of(2024, 1, 15); // 월
  private static final LocalDate WEEK_4 = LocalDate.of(2024, 1, 22); // 월
  private static final LocalDate WEDNESDAY = LocalDate.of(2024, 1, 10); // 수

  private FakeToDoRepository repository;
  private RoutineServiceImpl routineService;
  private Routine seededRoutine;

  @BeforeEach
  void setUp() {
    repository = new FakeToDoRepository();
    routineService = new RoutineServiceImpl(repository);
    seededRoutine = weeklyRoutine(WEEK_1, WEEK_4);

    seed("todo-w1", WEEK_1, false);
    seed("todo-w2", WEEK_2, false);
    seed("todo-w3", WEEK_3, false);
    seed("todo-w4", WEEK_4, false);
  }

  @Nested
  @DisplayName("삭제")
  class Delete {

    @Test
    @DisplayName("SINGLE — 선택한 투두 한 건만 삭제한다")
    void single() {
      routineService.deleteRoutineToDos(
          todoAt(WEEK_2), deleteCommand("todo-w2", RoutineDeleteType.SINGLE));

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_3, WEEK_4);
    }

    @Test
    @DisplayName("FROM_DATE — 선택한 날짜를 포함해서 이후를 모두 삭제한다")
    void fromDate() {
      routineService.deleteRoutineToDos(
          todoAt(WEEK_2), deleteCommand("todo-w2", RoutineDeleteType.FROM_DATE));

      // 요구사항: "선택한 투두 날짜 포함" — WEEK_2 도 함께 사라져야 한다.
      assertThat(remainingDates()).containsExactly(WEEK_1);
    }

    @Test
    @DisplayName("ALL — 반복 전체를 삭제한다")
    void all() {
      routineService.deleteRoutineToDos(
          todoAt(WEEK_2), deleteCommand("todo-w2", RoutineDeleteType.ALL));

      assertThat(remainingDates()).isEmpty();
    }
  }

  @Nested
  @DisplayName("수정")
  class Update {

    @Test
    @DisplayName("SINGLE — 선택한 투두만 바뀌고 나머지는 그대로다")
    void single() {
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand("todo-w2", WEEK_2, seededRoutine, RoutineUpdateType.SINGLE));

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
      assertThat(contentAt(WEEK_2)).isEqualTo(NEW_CONTENT);
      assertThat(contentAt(WEEK_1)).isEqualTo(OLD_CONTENT);
      assertThat(contentAt(WEEK_3)).isEqualTo(OLD_CONTENT);
    }

    @Test
    @DisplayName("FROM_DATE — 선택한 날짜를 포함해서 이후가 모두 바뀐다")
    void fromDate() {
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEEK_2, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
      assertThat(contentAt(WEEK_1)).isEqualTo(OLD_CONTENT);
      assertThat(contentAt(WEEK_2)).isEqualTo(NEW_CONTENT);
      assertThat(contentAt(WEEK_3)).isEqualTo(NEW_CONTENT);
      assertThat(contentAt(WEEK_4)).isEqualTo(NEW_CONTENT);
    }

    @Test
    @DisplayName("FROM_DATE — 날짜를 함께 옮겨도 기준선은 '선택한 투두의 원래 날짜'다 (회귀)")
    void fromDateWithMovedDate() {
      // 1/8 투두를 열어 1/10(수)로 옮기면서 "이후 전체 수정"을 누른 상황.
      // 기준선이 command.date(1/10)가 되면 1/8이 옛 내용으로 남아 요구사항을 위반한다.
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEDNESDAY, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));

      assertThat(datesOf(remaining())).doesNotContain(WEEK_2);
      assertThat(contentAt(WEEK_1)).isEqualTo(OLD_CONTENT);
      // 옮긴 요일(수)로 새 시리즈가 생성된다.
      assertThat(contentAt(WEDNESDAY)).isEqualTo(NEW_CONTENT);
    }

    @Test
    @DisplayName("ALL — 반복 전체가 바뀌되 완료 이력은 보존된다 (회귀)")
    void allPreservesCompletion() {
      repository.clear();
      seededRoutine = weeklyRoutine(WEEK_1, WEEK_4);
      seed("todo-w1", WEEK_1, true); // 완료
      seed("todo-w2", WEEK_2, true); // 완료
      seed("todo-w3", WEEK_3, false);
      seed("todo-w4", WEEK_4, false);

      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand("todo-w2", WEEK_2, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.ALL));

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
      assertThat(contentAt(WEEK_1)).isEqualTo(NEW_CONTENT);
      assertThat(completedAt(WEEK_1)).isTrue();
      assertThat(completedAt(WEEK_2)).isTrue();
      assertThat(completedAt(WEEK_3)).isFalse();
      assertThat(completedAt(WEEK_4)).isFalse();
    }

    @Test
    @DisplayName("FROM_DATE — 루틴 정보가 없으면 삭제하지 않고 단일 수정으로 축소한다")
    void fromDateWithoutRoutineIsNotDestructive() {
      routineService.updateRoutineToDos(
          todoAt(WEEK_2), updateCommand("todo-w2", WEEK_2, null, RoutineUpdateType.FROM_DATE));

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
      assertThat(contentAt(WEEK_2)).isEqualTo(NEW_CONTENT);
      assertThat(contentAt(WEEK_3)).isEqualTo(OLD_CONTENT);
    }

    @Test
    @DisplayName("ALL — 루틴 정보가 없으면 반복을 지우지 않고 단일 수정으로 축소한다")
    void allWithoutRoutineIsNotDestructive() {
      routineService.updateRoutineToDos(
          todoAt(WEEK_2), updateCommand("todo-w2", WEEK_2, null, RoutineUpdateType.ALL));

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
      assertThat(contentAt(WEEK_2)).isEqualTo(NEW_CONTENT);
    }
  }

  @Test
  @DisplayName("월말 투두에 매월 반복을 걸어도 예외 없이 처리된다 (회귀)")
  void monthlyRoutineOnMonthEndDoesNotThrow() {
    repository.clear();
    LocalDate monthEnd = LocalDate.of(2024, 1, 31);
    ToDo plainToDo =
        ToDo.builder()
            .id("todo-month-end")
            .userId(USER_ID)
            .goalId(GOAL_ID)
            .content(OLD_CONTENT)
            .date(monthEnd)
            .isCompleted(false)
            .isDeleted(false)
            .isImportant(false)
            .routine(null)
            .build();
    repository.saveToDo(plainToDo);

    Routine monthly =
        Routine.of(
            RoutineDuration.of(monthEnd, LocalDate.of(2024, 12, 31)), RepeatType.MONTHLY, null);

    // 2월에는 31일이 없어 다음 반복일 계산이 null 을 반환한다. 예전에는 여기서 NPE(HTTP 500)가 났다.
    assertThatCode(
            () ->
                routineService.updateRoutineToDos(
                    plainToDo,
                    updateCommand("todo-month-end", monthEnd, monthly, RoutineUpdateType.ALL)))
        .doesNotThrowAnyException();

    assertThat(contentAt(monthEnd)).isEqualTo(NEW_CONTENT);
  }

  // ---------- helpers ----------

  private Routine weeklyRoutine(LocalDate start, LocalDate end) {
    return Routine.of(RoutineDuration.of(start, end), RepeatType.WEEKLY, null);
  }

  private void seed(String id, LocalDate date, boolean completed) {
    repository.saveToDo(
        ToDo.builder()
            .id(id)
            .userId(USER_ID)
            .goalId(GOAL_ID)
            .content(OLD_CONTENT)
            .date(date)
            .isCompleted(completed)
            .isDeleted(false)
            .isImportant(false)
            .routine(seededRoutine)
            .build());
  }

  private UpdateToDoCommand updateCommand(
      String id, LocalDate date, Routine routine, RoutineUpdateType type) {
    return new UpdateToDoCommand(id, USER_ID, GOAL_ID, NEW_CONTENT, date, false, routine, type);
  }

  private DeleteToDoCommand deleteCommand(String id, RoutineDeleteType type) {
    return new DeleteToDoCommand(id, USER_ID, type);
  }

  private ToDo todoAt(LocalDate date) {
    return remaining().stream()
        .filter(todo -> todo.getDate().equals(date))
        .findFirst()
        .orElseThrow(() -> new AssertionError(date + " 에 투두가 없습니다"));
  }

  private List<ToDo> remaining() {
    return repository
        .findByUserIdAndDateRange(
            new GetDateRangeQueryFilter(
                USER_ID, LocalDate.of(2023, 1, 1), LocalDate.of(2025, 12, 31)))
        .stream()
        .sorted(java.util.Comparator.comparing(ToDo::getDate))
        .toList();
  }

  private List<LocalDate> remainingDates() {
    return datesOf(remaining());
  }

  private List<LocalDate> datesOf(List<ToDo> todos) {
    return todos.stream().map(ToDo::getDate).toList();
  }

  private String contentAt(LocalDate date) {
    return findAt(date).map(ToDo::getContent).orElse(null);
  }

  private boolean completedAt(LocalDate date) {
    return findAt(date).map(ToDo::isCompleted).orElse(false);
  }

  private Optional<ToDo> findAt(LocalDate date) {
    return remaining().stream().filter(todo -> todo.getDate().equals(date)).findFirst();
  }
}
