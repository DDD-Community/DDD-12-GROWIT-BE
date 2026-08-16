package com.growit.app.todo.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.FakeGoalQuery;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoQuery;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.FakeToDoValidator;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.service.RoutineServiceImpl;
import com.growit.app.todo.domain.service.ToDoValidator;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDuration;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateToDoUseCaseTest {

  private UpdateToDoUseCase updateToDoUseCase;
  private FakeToDoRepository fakeToDoRepository;
  private Goal goal;
  private ToDo toDo;

  @BeforeEach
  void setUp() {
    FakeGoalRepository fakeGoalRepository = new FakeGoalRepository();
    fakeToDoRepository = new FakeToDoRepository();
    FakeToDoQuery toDoQuery = new FakeToDoQuery(fakeToDoRepository);
    ToDoValidator toDoValidator = new FakeToDoValidator();
    FakeGoalQuery goalQuery = new FakeGoalQuery(fakeGoalRepository);
    updateToDoUseCase =
        new UpdateToDoUseCase(
            toDoQuery,
            toDoValidator,
            fakeToDoRepository,
            goalQuery,
            new RoutineServiceImpl(fakeToDoRepository));

    goal = GoalFixture.defaultGoal();
    fakeGoalRepository.saveGoal(goal);

    LocalDate today = LocalDate.now();

    toDo = ToDoFixture.customToDo("todo-1", goal.getUserId(), today, goal.getId());
    fakeToDoRepository.saveToDo(toDo);
  }

  @Test
  void givenToDoExists_whenUpdateToDo_thenReturnUpdatedResultAndRepositoryUpdated() {
    // Given
    LocalDate today = LocalDate.now();
    String newContent = "수정된 내용";
    UpdateToDoCommand command =
        new UpdateToDoCommand(
            toDo.getId(), toDo.getUserId(), toDo.getGoalId(), newContent, today, false, null, null);

    // When
    ToDoResult result = updateToDoUseCase.execute(command);

    // Then: 반환값 검증
    assertNotNull(result, "반환된 ToDoResult가 null이 아니어야 한다");
    assertEquals(toDo.getId(), result.getId(), "ToDoResult의 id가 수정 대상과 같아야 한다");

    // 저장소(Repository)에서도 실제로 값이 변경됐는지 검증
    ToDo updated = fakeToDoRepository.findById(toDo.getId()).orElse(null);
    assertNotNull(updated, "업데이트 후 ToDo는 null이 아니어야 한다");
    assertEquals(newContent, updated.getContent(), "ToDo 내용이 정상적으로 변경되어야 한다");
  }

  @Test
  void givenRoutineScopeWithoutRoutine_whenUpdateToDo_thenThrowsBadRequest() {
    // Given: FROM_DATE/ALL 은 대상 투두를 지우고 다시 만드는 방식이라 루틴 정보가 없으면 대량 삭제가 된다.
    UpdateToDoCommand command =
        new UpdateToDoCommand(
            toDo.getId(),
            toDo.getUserId(),
            toDo.getGoalId(),
            "수정된 내용",
            LocalDate.now(),
            false,
            null,
            RoutineUpdateType.ALL);

    // When & Then
    assertThrows(BadRequestException.class, () -> updateToDoUseCase.execute(command));

    ToDo untouched = fakeToDoRepository.findById(toDo.getId()).orElse(null);
    assertNotNull(untouched, "요청이 거절되면 기존 ToDo는 그대로 남아야 한다");
  }

  @Test
  void givenPlainToDoAndRoutineWithoutScope_whenUpdateToDo_thenThrowsBadRequest() {
    // Given: 반복이 없던 투두를 반복으로 바꾸면서 범위를 생략한 요청.
    //        거절하지 않으면 대상 투두에 routine 만 붙고 나머지 회차는 생성되지 않아,
    //        200 을 받은 클라이언트가 "반복이 걸렸다"고 오해한다.
    LocalDate start = LocalDate.now();
    UpdateToDoCommand command = toRoutineCommand(start, null);

    // When & Then
    assertThrows(BadRequestException.class, () -> updateToDoUseCase.execute(command));

    List<ToDo> remaining = allToDos(start);
    assertEquals(1, remaining.size(), "거절된 요청은 회차를 생성하지 않아야 한다");
    assertNull(remaining.get(0).getRoutine(), "거절된 요청은 반복을 붙이지 않아야 한다");
  }

  @Test
  void givenPlainToDoAndRoutineWithScope_whenUpdateToDo_thenCreatesEveryOccurrence() {
    // Given: 같은 요청에 범위를 명시하면 종료일까지의 회차가 실제로 생성된다.
    LocalDate start = LocalDate.now();
    UpdateToDoCommand command = toRoutineCommand(start, RoutineUpdateType.ALL);

    // When
    updateToDoUseCase.execute(command);

    // Then: 시작일 + 3주 = 4회차
    List<ToDo> remaining = allToDos(start);
    assertEquals(4, remaining.size(), "범위를 지정하면 종료일까지 회차가 생성돼야 한다");
    assertTrue(remaining.stream().allMatch(it -> it.getRoutine() != null), "생성된 회차는 모두 반복에 속해야 한다");
  }

  private UpdateToDoCommand toRoutineCommand(LocalDate start, RoutineUpdateType type) {
    Routine weekly =
        Routine.of(RoutineDuration.of(start, start.plusWeeks(3)), RepeatType.WEEKLY, null);

    return new UpdateToDoCommand(
        toDo.getId(), toDo.getUserId(), toDo.getGoalId(), "매주 반복으로 전환", start, false, weekly, type);
  }

  private List<ToDo> allToDos(LocalDate around) {
    return fakeToDoRepository.findByUserIdAndDateRange(
        new GetDateRangeQueryFilter(toDo.getUserId(), around.minusYears(1), around.plusYears(1)));
  }
}
