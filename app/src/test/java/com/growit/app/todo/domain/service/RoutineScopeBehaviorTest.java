package com.growit.app.todo.domain.service;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.app.common.exception.BadRequestException;
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
  private static final java.time.DayOfWeek WEDNESDAY_DOW = java.time.DayOfWeek.WEDNESDAY;

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

      // 1/1 은 옛 내용으로 남고, 1/8 이후는 전부 지워진 뒤 옮긴 요일(수)로 새 시리즈가 생성된다.
      assertThat(remainingDates()).containsExactly(WEEK_1, WEDNESDAY, LocalDate.of(2024, 1, 17));
      assertThat(contentAt(WEEK_1)).isEqualTo(OLD_CONTENT);
      assertThat(contentAt(WEDNESDAY)).isEqualTo(NEW_CONTENT);
      assertThat(contentAt(LocalDate.of(2024, 1, 17))).isEqualTo(NEW_CONTENT);
    }

    @Test
    @DisplayName("일정이 그대로면 ID 가 유지되고, 일정을 바꾸면 새로 생성돼 ID 가 바뀐다")
    void idStabilityDependsOnScheduleChange() {
      // 내용만 바꾸는 흔한 경우 — 지우고 다시 만들지 않으므로 ID 가 살아있다.
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand("todo-w2", WEEK_2, seededRoutine, RoutineUpdateType.SINGLE));
      assertThat(todoAt(WEEK_2).getId()).isEqualTo("todo-w2");

      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEEK_2, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));
      assertThat(todoAt(WEEK_2).getId()).isEqualTo("todo-w2");

      // 날짜(요일)를 옮기면 일정이 바뀐 것이라 재생성되고 ID 가 바뀐다.
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEDNESDAY, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));
      assertThat(datesOf(remaining())).doesNotContain(WEEK_2);
      assertThat(todoAt(WEDNESDAY).getId()).isNotEqualTo("todo-w2");
    }

    @Test
    @DisplayName("FROM_DATE 로 재생성되어도 완료 이력은 보존된다")
    void fromDatePreservesCompletion() {
      repository.clear();
      seededRoutine = weeklyRoutine(WEEK_1, WEEK_4);
      seed("todo-w1", WEEK_1, false);
      seed("todo-w2", WEEK_2, true); // 완료
      seed("todo-w3", WEEK_3, false);
      seed("todo-w4", WEEK_4, false);

      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEEK_2, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));

      assertThat(completedAt(WEEK_2)).isTrue();
      assertThat(completedAt(WEEK_3)).isFalse();
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
    @DisplayName("FROM_DATE — 루틴 정보가 없으면 거절하고 아무것도 지우지 않는다")
    void fromDateWithoutRoutineIsRejected() {
      ToDo target = todoAt(WEEK_2);

      assertThatThrownBy(
              () ->
                  routineService.updateRoutineToDos(
                      target, updateCommand("todo-w2", WEEK_2, null, RoutineUpdateType.FROM_DATE)))
          .isInstanceOf(BadRequestException.class);

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
      assertThat(contentAt(WEEK_2)).isEqualTo(OLD_CONTENT);
    }

    @Test
    @DisplayName("ALL — 루틴 정보가 없으면 거절하고 반복을 지우지 않는다")
    void allWithoutRoutineIsRejected() {
      ToDo target = todoAt(WEEK_2);

      assertThatThrownBy(
              () ->
                  routineService.updateRoutineToDos(
                      target, updateCommand("todo-w2", WEEK_2, null, RoutineUpdateType.ALL)))
          .isInstanceOf(BadRequestException.class);

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
    }
  }

  @Nested
  @DisplayName("데이터 손실 방지")
  class DataLossGuards {

    @Test
    @DisplayName("FROM_DATE 로 나눈 뒤 앞쪽 시리즈에서 ALL 을 눌러도 투두가 중복되지 않는다")
    void precedingSeriesAllDoesNotDuplicate() {
      // 1/8 에서 나누면 1/1 은 앞쪽 시리즈에 남는다. 앞쪽 반복이 원래의 넓은 기간(1/1~1/22)을
      // 그대로 들고 있으면, 1/1 에서 ALL 을 누를 때 뒤쪽이 이미 차지한 1/8·1/15·1/22 를 다시 만든다.
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEDNESDAY, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));

      ToDo front = todoAt(WEEK_1);
      assertThat(front.getRoutine().getDuration().getEndDate())
          .as("앞쪽 반복은 기준일 직전까지로 좁혀져야 한다")
          .isEqualTo(WEEK_2.minusDays(1));

      Routine echoed = front.getRoutine();
      routineService.updateRoutineToDos(
          front,
          updateCommand(
              front.getId(),
              WEEK_1,
              Routine.of(
                  RoutineDuration.of(
                      echoed.getDuration().getStartDate(), echoed.getDuration().getEndDate()),
                  echoed.getRepeatType(),
                  echoed.getRepeatDays()),
              RoutineUpdateType.ALL));

      assertThat(remainingDates()).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("요일을 바꾸면서 종료일을 당겨 회차가 0건이 되면 거절하고 아무것도 지우지 않는다")
    void emptyRegenerationIsRejected() {
      ToDo target = todoAt(WEEK_2);
      Routine sundayOnly =
          Routine.of(
              RoutineDuration.of(WEEK_1, LocalDate.of(2024, 1, 9)),
              RepeatType.WEEKLY,
              List.of(SUNDAY));

      assertThatThrownBy(
              () ->
                  routineService.updateRoutineToDos(
                      target,
                      updateCommand("todo-w2", WEEK_2, sundayOnly, RoutineUpdateType.FROM_DATE)))
          .isInstanceOf(BadRequestException.class);

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
    }

    @Test
    @DisplayName("앞쪽 기간은 좁히기만 한다 — 원래 종료일보다 뒤로 늘어나지 않는다")
    void narrowingNeverWidensPrecedingSeries() {
      // 마지막 회차를 반복 기간(~1/22) 밖인 2/12 로 옮겨둔다. SINGLE 은 날짜를 자유롭게 바꾼다.
      LocalDate moved = LocalDate.of(2024, 2, 12);
      routineService.updateRoutineToDos(
          todoAt(WEEK_4), updateCommand("todo-w4", moved, seededRoutine, RoutineUpdateType.SINGLE));

      // 옮긴 회차에서 요일을 바꿔 재생성(분리) 경로로 보낸다. 기준일이 2/12 이므로
      // 좁히기를 그대로 두면 앞쪽 기간이 2/11 까지 늘어나 없던 회차가 생길 수 있다.
      routineService.updateRoutineToDos(
          todoAt(moved),
          updateCommand(
              "todo-w4",
              moved,
              Routine.of(
                  RoutineDuration.of(WEEK_1, LocalDate.of(2024, 3, 31)),
                  RepeatType.WEEKLY,
                  List.of(WEDNESDAY_DOW)),
              RoutineUpdateType.FROM_DATE));

      assertThat(todoAt(WEEK_1).getRoutine().getDuration().getEndDate())
          .as("앞쪽 반복은 원래 종료일을 넘지 않아야 한다")
          .isEqualTo(WEEK_4);
    }

    @Test
    @DisplayName("반복 기간보다 뒤인 투두에 반복을 걸면 거절한다")
    void attachRoutineAfterDurationIsRejected() {
      repository.clear();
      LocalDate after = LocalDate.of(2024, 3, 1);
      ToDo plain = plainToDo("todo-after", after);
      repository.saveToDo(plain);

      Routine january = weeklyRoutine(WEEK_1, LocalDate.of(2024, 1, 31));

      assertThatThrownBy(
              () ->
                  routineService.updateRoutineToDos(
                      plain, updateCommand("todo-after", after, january, RoutineUpdateType.ALL)))
          .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("반복 기간 밖의 투두에 반복을 걸면 거절한다")
    void attachRoutineOutsideDurationIsRejected() {
      repository.clear();
      LocalDate outside = LocalDate.of(2024, 1, 1);
      ToDo plain = plainToDo("todo-outside", outside);
      repository.saveToDo(plain);

      Routine february =
          Routine.of(
              RoutineDuration.of(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28)),
              RepeatType.WEEKLY,
              null);

      assertThatThrownBy(
              () ->
                  routineService.updateRoutineToDos(
                      plain,
                      updateCommand("todo-outside", outside, february, RoutineUpdateType.ALL)))
          .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("FROM_DATE 로 시리즈를 나눈 뒤 뒤쪽에서 ALL 을 눌러도 투두가 중복 생성되지 않는다")
    void fromDateThenAllDuplicates() {
      // 1) FROM_DATE 로 1/8 이후를 새 루틴으로 분리한다. 클라이언트는 조회 응답의 duration(1/1~1/22)을 그대로 보낸다.
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEDNESDAY, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));

      // 분리 후 서버가 돌려주는 루틴 기간은 1/1~1/22 가 아니라 1/8~1/22 로 좁혀져 있어야 한다.
      // 이걸 원본 그대로(1/1 시작) 저장하면, 뒤쪽 시리즈에서 ALL 을 눌렀을 때 1/1 부터 다시 생성해
      // 앞쪽 시리즈에 남아있는 1/1 과 같은 날에 투두가 2건이 된다.
      ToDo afterSplit = todoAt(LocalDate.of(2024, 1, 17));
      assertThat(afterSplit.getRoutine().getDuration().getStartDate()).isEqualTo(WEEK_2);

      // 2) 클라이언트는 조회 응답의 routine 을 그대로 되돌려보낸다 (API 문서가 안내하는 방식).
      Routine echoed = afterSplit.getRoutine();
      routineService.updateRoutineToDos(
          afterSplit,
          updateCommand(
              afterSplit.getId(),
              LocalDate.of(2024, 1, 17),
              Routine.of(
                  RoutineDuration.of(
                      echoed.getDuration().getStartDate(), echoed.getDuration().getEndDate()),
                  echoed.getRepeatType(),
                  echoed.getRepeatDays()),
              RoutineUpdateType.ALL));

      assertThat(remainingDates()).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("FROM_DATE 로 종료일을 기준일보다 앞당기면 거절한다 (삭제만 되고 재생성 0건이 되는 것을 막는다)")
    void fromDateWithEndDateBeforeCutoffIsRejected() {
      // 1/15 투두를 열어 반복 종료일을 1/10 으로 앞당기고 "이후 전체 수정"을 누른 상황.
      ToDo target = todoAt(WEEK_3);

      assertThatThrownBy(
              () ->
                  routineService.updateRoutineToDos(
                      target,
                      updateCommand(
                          "todo-w3",
                          WEEK_3,
                          weeklyRoutine(WEEK_1, LocalDate.of(2024, 1, 10)),
                          RoutineUpdateType.FROM_DATE)))
          .isInstanceOf(BadRequestException.class);

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
    }

    @Test
    @DisplayName("월말 매월 반복은 매달 월말 자리를 지키며 생성된다")
    void monthEndMonthlyCreatesEveryOccurrence() {
      repository.clear();
      LocalDate monthEnd = LocalDate.of(2024, 1, 31);
      ToDo plain = plainToDo("todo-month-end", monthEnd);
      repository.saveToDo(plain);

      Routine monthly =
          Routine.of(
              RoutineDuration.of(monthEnd, LocalDate.of(2024, 12, 31)), RepeatType.MONTHLY, null);

      routineService.updateRoutineToDos(
          plain, updateCommand("todo-month-end", monthEnd, monthly, RoutineUpdateType.ALL));

      // 월말 보정이 제대로 되면 2/29 -> 3/31 -> 4/30 순으로 원래 "월말" 자리를 지킨다.
      // size 만 보면 2/29 이후 29일로 굳어지는 회귀를 잡지 못한다.
      assertThat(remainingDates())
          .containsExactly(
              monthEnd,
              LocalDate.of(2024, 2, 29),
              LocalDate.of(2024, 3, 31),
              LocalDate.of(2024, 4, 30),
              LocalDate.of(2024, 5, 31),
              LocalDate.of(2024, 6, 30),
              LocalDate.of(2024, 7, 31),
              LocalDate.of(2024, 8, 31),
              LocalDate.of(2024, 9, 30),
              LocalDate.of(2024, 10, 31),
              LocalDate.of(2024, 11, 30),
              LocalDate.of(2024, 12, 31));
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

  @Nested
  @DisplayName("일정을 바꿔 재생성될 때")
  class Rescheduled {

    /** 재생성 경로에서만 도는 완료 복원 로직을 검증한다. 일정이 그대로면 제자리 수정이라 이 로직을 타지 않으므로, 여기서는 종료일을 늘려 반드시 재생성되게 한다. */
    @Test
    @DisplayName("ALL — 재생성돼도 살아남는 날짜의 완료 이력은 이어받는다")
    void allCarriesCompletionAcrossRegeneration() {
      repository.clear();
      seededRoutine = weeklyRoutine(WEEK_1, WEEK_4);
      seed("todo-w1", WEEK_1, true); // 완료
      seed("todo-w2", WEEK_2, false);
      seed("todo-w3", WEEK_3, true); // 완료
      seed("todo-w4", WEEK_4, false);

      LocalDate extended = LocalDate.of(2024, 1, 29);
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand("todo-w2", WEEK_2, weeklyRoutine(WEEK_1, extended), RoutineUpdateType.ALL));

      // 종료일이 바뀌었으므로 전부 지워졌다가 다시 만들어진다. ID 는 바뀌지만 완료 표시는 날짜로 따라온다.
      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4, extended);
      assertThat(todoAt(WEEK_1).getId()).isNotEqualTo("todo-w1");
      assertThat(completedAt(WEEK_1)).isTrue();
      assertThat(completedAt(WEEK_3)).isTrue();
      assertThat(completedAt(WEEK_2)).isFalse();
      assertThat(completedAt(WEEK_4)).isFalse();
      assertThat(completedAt(extended)).isFalse();
    }

    @Test
    @DisplayName("FROM_DATE — 요일을 옮기면 날짜가 어긋난 완료 이력은 사라진다")
    void fromDateDropsCompletionWhenDatesShift() {
      repository.clear();
      seededRoutine = weeklyRoutine(WEEK_1, WEEK_4);
      seed("todo-w1", WEEK_1, false);
      seed("todo-w2", WEEK_2, true); // 완료 — 월요일
      seed("todo-w3", WEEK_3, false);
      seed("todo-w4", WEEK_4, false);

      // 수요일로 옮기면 새 회차 날짜(1/10, 1/17)가 옛 완료 날짜(1/8)와 겹치지 않는다.
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEDNESDAY, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));

      assertThat(remainingDates()).containsExactly(WEEK_1, WEDNESDAY, LocalDate.of(2024, 1, 17));
      assertThat(completedAt(WEDNESDAY)).isFalse();
      assertThat(completedAt(LocalDate.of(2024, 1, 17))).isFalse();
    }

    @Test
    @DisplayName("반복 주기만 바꿔도 재생성된다")
    void repeatTypeChangeTriggersRegeneration() {
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2",
              WEEK_2,
              Routine.of(RoutineDuration.of(WEEK_1, WEEK_4), RepeatType.BIWEEKLY, null),
              RoutineUpdateType.FROM_DATE));

      // 주간 -> 격주: 1/8, 1/22 만 남는다. 제자리 수정이었다면 1/15 가 그대로 있었을 것이다.
      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_4);
    }

    @Test
    @DisplayName("요일 지정만 바꿔도 재생성된다")
    void repeatDaysChangeTriggersRegeneration() {
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2",
              WEEK_2,
              Routine.of(
                  RoutineDuration.of(WEEK_1, WEEK_4), RepeatType.WEEKLY, List.of(WEDNESDAY_DOW)),
              RoutineUpdateType.FROM_DATE));

      // 월요일 시리즈가 수요일 시리즈로 바뀐다.
      assertThat(remainingDates()).containsExactly(WEEK_1, WEDNESDAY, LocalDate.of(2024, 1, 17));
    }

    @Test
    @DisplayName("시작일만 바꿔도 재생성된다")
    void startDateChangeTriggersRegeneration() {
      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand("todo-w2", WEEK_2, weeklyRoutine(WEEK_2, WEEK_4), RoutineUpdateType.ALL));

      // 시작일을 1/8 로 늦추면 1/1 회차가 사라진다. 제자리 수정이었다면 1/1 이 그대로 남았을 것이다.
      assertThat(remainingDates()).containsExactly(WEEK_2, WEEK_3, WEEK_4);
    }

    @Test
    @DisplayName("반복 기간이 너무 길면 거절한다")
    void tooManyOccurrencesIsRejected() {
      ToDo target = todoAt(WEEK_2);
      Routine tenYearsDaily =
          Routine.of(RoutineDuration.of(WEEK_1, LocalDate.of(2034, 1, 1)), RepeatType.DAILY, null);

      assertThatThrownBy(
              () ->
                  routineService.updateRoutineToDos(
                      target,
                      updateCommand("todo-w2", WEEK_2, tenYearsDaily, RoutineUpdateType.ALL)))
          .isInstanceOf(BadRequestException.class);

      assertThat(remainingDates()).containsExactly(WEEK_1, WEEK_2, WEEK_3, WEEK_4);
    }
  }

  @Nested
  @DisplayName("일정이 그대로면 제자리 수정")
  class KeepsSchedule {

    @Test
    @DisplayName("FROM_DATE — 내용만 바꾸면 투두 ID 와 완료 이력이 그대로 유지된다")
    void fromDateKeepsIdsAndCompletion() {
      repository.clear();
      seededRoutine = weeklyRoutine(WEEK_1, WEEK_4);
      seed("todo-w1", WEEK_1, false);
      seed("todo-w2", WEEK_2, true);
      seed("todo-w3", WEEK_3, false);
      seed("todo-w4", WEEK_4, false);

      routineService.updateRoutineToDos(
          todoAt(WEEK_2),
          updateCommand(
              "todo-w2", WEEK_2, weeklyRoutine(WEEK_1, WEEK_4), RoutineUpdateType.FROM_DATE));

      // 지우고 다시 만들지 않으므로 ID 가 살아있다.
      assertThat(todoAt(WEEK_2).getId()).isEqualTo("todo-w2");
      assertThat(todoAt(WEEK_4).getId()).isEqualTo("todo-w4");
      assertThat(completedAt(WEEK_2)).isTrue();
      assertThat(contentAt(WEEK_1)).isEqualTo(OLD_CONTENT);
      assertThat(contentAt(WEEK_2)).isEqualTo(NEW_CONTENT);
      assertThat(contentAt(WEEK_4)).isEqualTo(NEW_CONTENT);
    }

    @Test
    @DisplayName("격주 반복은 제자리 수정이라 홀짝 주기가 어긋나지 않는다")
    void biweeklyKeepsParity() {
      repository.clear();
      LocalDate end = LocalDate.of(2024, 2, 26);
      seededRoutine =
          Routine.of(RoutineDuration.of(WEEK_1, end), RepeatType.BIWEEKLY, List.of(MONDAY));
      seed("b1", WEEK_1, false);
      seed("b2", LocalDate.of(2024, 1, 15), false);
      seed("b3", LocalDate.of(2024, 1, 29), false);
      seed("b4", LocalDate.of(2024, 2, 12), false);
      seed("b5", end, false);

      routineService.updateRoutineToDos(
          todoAt(LocalDate.of(2024, 1, 15)),
          updateCommand(
              "b2",
              LocalDate.of(2024, 1, 15),
              Routine.of(RoutineDuration.of(WEEK_1, end), RepeatType.BIWEEKLY, List.of(MONDAY)),
              RoutineUpdateType.FROM_DATE));

      assertThat(remainingDates())
          .containsExactly(
              WEEK_1,
              LocalDate.of(2024, 1, 15),
              LocalDate.of(2024, 1, 29),
              LocalDate.of(2024, 2, 12),
              end);
    }
  }

  @Nested
  @DisplayName("다른 반복·다른 사용자 격리")
  class Isolation {

    @Test
    @DisplayName("FROM_DATE 삭제는 같은 반복만 건드리고 다른 반복·다른 사용자는 남긴다")
    void deleteFromDateTouchesOnlyOwnSeries() {
      Routine other = weeklyRoutine(WEEK_1, WEEK_4);
      repository.saveToDo(
          ToDo.builder()
              .id("other-routine")
              .userId(USER_ID)
              .goalId(GOAL_ID)
              .content("다른 반복")
              .date(WEEK_3)
              .isCompleted(false)
              .isDeleted(false)
              .isImportant(false)
              .routine(other)
              .build());
      repository.saveToDo(
          ToDo.builder()
              .id("standalone")
              .userId(USER_ID)
              .goalId(GOAL_ID)
              .content("반복 아님")
              .date(WEEK_4)
              .isCompleted(false)
              .isDeleted(false)
              .isImportant(false)
              .routine(null)
              .build());

      routineService.deleteRoutineToDos(
          todoAt(WEEK_2), deleteCommand("todo-w2", RoutineDeleteType.FROM_DATE));

      List<String> ids = remaining().stream().map(ToDo::getId).toList();
      assertThat(ids).contains("todo-w1", "other-routine", "standalone");
      assertThat(ids).doesNotContain("todo-w2", "todo-w3", "todo-w4");
    }
  }

  // ---------- helpers ----------

  private ToDo plainToDo(String id, LocalDate date) {
    return ToDo.builder()
        .id(id)
        .userId(USER_ID)
        .goalId(GOAL_ID)
        .content(OLD_CONTENT)
        .date(date)
        .isCompleted(false)
        .isDeleted(false)
        .routine(null)
        .build();
  }

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
            .routine(seededRoutine)
            .build());
  }

  private UpdateToDoCommand updateCommand(
      String id, LocalDate date, Routine routine, RoutineUpdateType type) {
    return new UpdateToDoCommand(
        id, USER_ID, GOAL_ID, NEW_CONTENT, date, null, null, routine, type);
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
    return findAt(date).orElseThrow(() -> new AssertionError(date + " 에 투두가 없습니다")).isCompleted();
  }

  private Optional<ToDo> findAt(LocalDate date) {
    return remaining().stream().filter(todo -> todo.getDate().equals(date)).findFirst();
  }
}
